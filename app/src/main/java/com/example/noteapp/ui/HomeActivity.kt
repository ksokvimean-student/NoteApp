package com.example.noteapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.R
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.data.database.NoteDao
import com.example.noteapp.data.database.SettingsManager
import com.example.noteapp.databinding.ActivityHomeBinding
import com.example.noteapp.databinding.UserProfileBinding
import com.example.noteapp.model.Note
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var noteDao: NoteDao
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var settingsManager: SettingsManager

    private var name: String? = ""
    private var email: String? = ""
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", 0)
        name = intent.getStringExtra("USER_NAME")
        email = intent.getStringExtra("USER_EMAIL")

        noteDao = NoteDao(this)
        settingsManager = SettingsManager(this)
        binding.bottomNavLayout.bottomNavigation.setupNavigation(this, R.id.nav_home)

        setupRecyclerView()
        setupClickListeners()
        setupSearch()
    }
    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            val query = text?.toString() ?: ""

            val count = noteAdapter.filter(query)

            if (query.isNotEmpty() && count == 0) {
                // Searching but nothing found
                binding.tvNoResults.visibility = View.VISIBLE
                binding.rvNotes.visibility = View.GONE
            } else if (query.isEmpty() && noteAdapter.itemCount == 0) {
                // Not searching, but database is empty
                binding.tvNoResults.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvNotes.visibility = View.GONE
            } else {
                // Results found or search is cleared
                binding.tvNoResults.visibility = View.GONE
                binding.tvEmpty.visibility = View.GONE
                binding.rvNotes.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavLayout.bottomNavigation.selectedItemId = R.id.nav_home
        loadNotes()
    }

    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            notes = emptyList(),
            onNoteClick = { note ->
                openUpdateNoteActivity(note.id)
            },
            onDeleteClick = { note ->
                showDeleteConfirmation(note)
            }
        )

        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            adapter = noteAdapter
        }
    }

    private fun setupClickListeners() {
        binding.ivUserProfile.setOnClickListener {
            showUserProfileSheet()
        }

        binding.fabAddNote.setOnClickListener {
            openUpdateNoteActivity(null)
        }
    }

    private fun loadNotes() {
        val notes = noteDao.getAllByUser(userId)
        noteAdapter.updateNotes(notes)

        // Handle Visibility
        if (notes.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.rvNotes.visibility = View.GONE
            binding.tilSearch.visibility = View.GONE // Hide search if no notes exist
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.rvNotes.visibility = View.VISIBLE
            binding.tilSearch.visibility = View.VISIBLE // Show search if notes exist
        }
    }

    private fun openUpdateNoteActivity(noteId: Int?) {
        val intent = Intent(this, UpdateNoteActivity::class.java).apply {
            putExtra("USER_ID", userId)
            noteId?.let { putExtra("NOTE_ID", it) }
        }
        startActivity(intent)
    }

    private fun showDeleteConfirmation(note: Note) {
        if (!settingsManager.isConfirmDeleteEnabled) {
            noteDao.delete(note)
            loadNotes()
            return
        }

        val dialog = BottomSheetDialog(this)
        val sheetBinding = UserProfileBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)

        sheetBinding.tvSheetName.text = "Delete Note?"
        sheetBinding.tvSheetEmail.text = "Are you sure you want to delete \"${note.title}\"?"
        sheetBinding.btnLogout.text = "Delete"

        sheetBinding.btnLogout.setOnClickListener {
            noteDao.delete(note)
            loadNotes()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showUserProfileSheet() {
        val dialog = BottomSheetDialog(this)
        val sheetBinding = UserProfileBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)

        sheetBinding.tvSheetName.text = if (name.isNullOrEmpty()) "User" else name
        sheetBinding.tvSheetEmail.text = if (email.isNullOrEmpty()) "No Email" else email
        sheetBinding.btnLogout.text = "Logout"

        sheetBinding.btnLogout.setOnClickListener {
            // Clear Login Session
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean("is_logged_in", false).apply()

            // Navigate to Login
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            dialog.dismiss()
            startActivity(intent)
            finish()
        }

        dialog.show()
    }
}