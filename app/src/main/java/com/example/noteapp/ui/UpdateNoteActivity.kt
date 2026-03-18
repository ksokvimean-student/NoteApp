package com.example.noteapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.noteapp.R
import com.example.noteapp.data.database.NoteDao
import com.example.noteapp.data.database.SettingsManager
import com.example.noteapp.databinding.ActivityUpdateNoteBinding
import com.example.noteapp.model.Note

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var noteDao: NoteDao
    private lateinit var settingsManager: SettingsManager

    private var userId: Int = 0
    private var noteId: Int? = null
    private var existingNote: Note? = null
    private var isEditMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init data
        userId = intent.getIntExtra("USER_ID", 0)
        val idVal = intent.getIntExtra("NOTE_ID", -1)
        noteId = if (idVal != -1) idVal else null
        isEditMode = noteId != null

        noteDao = NoteDao(this)
        settingsManager = SettingsManager(this)

        if (isEditMode) {
            loadExistingNote()
        }

        setupUI()
        setupClickListeners()
        setupBackPressHandler()
    }

    private fun setupUI() {
        if (isEditMode) {
            binding.tvTitle.text = "Edit Note"
            binding.btnSave.setImageResource(R.drawable.ic_edit) // Floppy icon
            binding.btnDelete.visibility = View.VISIBLE
        } else {
            binding.tvTitle.text = "New Note"
            binding.btnSave.setImageResource(R.drawable.ic_add) // Checkmark icon
            binding.btnDelete.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        // Back Icon (Left)
        binding.topAppBar.setNavigationOnClickListener {
            handleExit()
        }

        // Save Icon (Right)
        binding.btnSave.setOnClickListener {
            saveNote()
        }

        // Delete Icon (Right)
        binding.btnDelete.setOnClickListener {
            existingNote?.let { note ->
                noteDao.delete(note)
                Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleExit()
            }
        })
    }

    private fun handleExit() {
        if (settingsManager.isAutoSaveEnabled) {
            saveNote(silent = true)
        } else {
            finish()
        }
    }

    private fun loadExistingNote() {
        noteId?.let { id ->
            existingNote = noteDao.getNoteById(id)
            existingNote?.let { note ->
                binding.etTitle.setText(note.title)
                binding.etContent.setText(note.content)
            }
        }
    }

    private fun saveNote(silent: Boolean = false) {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isEmpty()) {
            if (!silent) binding.tilTitle.error = "Title is required"
            else finish()
            return
        }

        if (isEditMode && existingNote != null) {
            val updatedNote = existingNote!!.copy(title = title, content = content)
            noteDao.update(updatedNote)
            if (!silent) Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
        } else {
            val newNote = Note(userId = userId, title = title, content = content)
            noteDao.insert(newNote)
            if (!silent) Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}