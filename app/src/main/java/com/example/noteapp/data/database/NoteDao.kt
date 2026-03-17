package com.example.noteapp.data.database

import android.content.Context
import com.example.noteapp.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteDao(private val context: Context) {

    private val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    private val settingsManager = SettingsManager(context)

    companion object {
        private const val PREFS_NAME = "notes_prefs"
        private const val KEY_NOTES = "notes_list"
    }

    private fun getNotesDir(): File {
        val folderName = settingsManager.storageFolder
        val dir = File(context.filesDir, folderName)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    private fun getNotesFile(): File {
        return File(context.filesDir, "notes_metadata.json")
    }

    private fun sanitizeFileName(title: String, id: Int): String {
        val sanitized = title.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        return "${id}_$sanitized.txt"
    }

    private fun generateId(): Int {
        return System.currentTimeMillis().toInt()
    }

    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun readNotesFromPrefs(): MutableList<Note> {
        val json = getNotesFile().readTextOrNull() ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Note>>() {}.type
        return try {
            gson.fromJson(json, type) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    private fun saveNotesToPrefs(notes: List<Note>) {
        val json = gson.toJson(notes)
        getNotesFile().writeText(json)
    }

    fun insert(note: Note): Note {
        val id = generateId()
        val timestamp = getCurrentTimestamp()
        val fileName = sanitizeFileName(note.title, id)
        val filePath = File(getNotesDir(), fileName).absolutePath

        val newNote = note.copy(
            id = id,
            filePath = filePath,
            createdAt = timestamp,
            updatedAt = timestamp
        )

        File(filePath).writeText(note.content)

        val notes = readNotesFromPrefs()
        notes.add(newNote)
        saveNotesToPrefs(notes)

        return newNote
    }

    fun getAllByUser(userId: Int): List<Note> {
        val notes = readNotesFromPrefs()
        return notes
            .filter { it.userId == userId }
            .map { note ->
                val content = try {
                    File(note.filePath).readTextOrNull() ?: ""
                } catch (e: Exception) {
                    ""
                }
                note.copy(content = content)
            }
            .sortedByDescending { it.updatedAt }
    }

    fun getNoteById(noteId: Int): Note? {
        val notes = readNotesFromPrefs()
        val note = notes.find { it.id == noteId }
        return note?.let {
            val content = try {
                File(it.filePath).readTextOrNull() ?: ""
            } catch (e: Exception) {
                ""
            }
            it.copy(content = content)
        }
    }

    fun update(note: Note): Boolean {
        val notes = readNotesFromPrefs()
        val index = notes.indexOfFirst { it.id == note.id }

        if (index == -1) return false

        val timestamp = getCurrentTimestamp()
        val updatedNote = note.copy(updatedAt = timestamp)

        File(note.filePath).writeText(note.content)

        notes[index] = updatedNote
        saveNotesToPrefs(notes)

        return true
    }

    fun delete(note: Note): Boolean {
        val notes = readNotesFromPrefs()
        val index = notes.indexOfFirst { it.id == note.id }

        if (index == -1) return false

        try {
            File(note.filePath).delete()
        } catch (e: Exception) {
            // Ignore file deletion errors
        }

        notes.removeAt(index)
        saveNotesToPrefs(notes)

        return true
    }

    private fun File.readTextOrNull(): String? {
        return try {
            if (exists()) readText() else null
        } catch (e: Exception) {
            null
        }
    }
}
