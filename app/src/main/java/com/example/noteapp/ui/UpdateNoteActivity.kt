package com.example.noteapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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

        userId = intent.getIntExtra("USER_ID", 0)
        noteId = intent.getIntExtra("NOTE_ID", -1).takeIf { it != -1 }

        noteDao = NoteDao(this)
        settingsManager = SettingsManager(this)

        isEditMode = noteId != null

        if (isEditMode) {
            loadExistingNote()
        }

        setupUI()
        setupToolbar()
        setupBackPressHandler()
    }

    private fun setupToolbar() {
        binding.topAppBar.setNavigationOnClickListener {
            if (settingsManager.isAutoSaveEnabled) {
                saveNote()
            } else {
                finish()
            }
        }
    }

    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (settingsManager.isAutoSaveEnabled) {
                    saveNote()
                } else {
                    finish()
                }
            }
        })
    }

    private fun setupUI() {
        binding.tvTitle.text = if (isEditMode) "Edit Note" else "New Note"
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

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isEmpty()) {
            binding.tilTitle.error = "Title is required"
            return
        }

        binding.tilTitle.error = null

        if (isEditMode && existingNote != null) {
            val updatedNote = existingNote!!.copy(
                title = title,
                content = content
            )
            noteDao.update(updatedNote)
            Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
        } else {
            val newNote = Note(
                userId = userId,
                title = title,
                content = content
            )
            noteDao.insert(newNote)
            Toast.makeText(this, "Note saved!", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}
