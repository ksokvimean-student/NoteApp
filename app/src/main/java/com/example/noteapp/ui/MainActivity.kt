package com.example.noteapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.adapter.NoteAdapter
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.model.Note

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Static Data Source
        val noteList = listOf(
            Note(1, "Shopping List", "Buy milk and bread"),
            Note(2, "Meeting Notes", "Project discussion at 3 PM"),
            Note(3, "Study Plan", "Learn Kotlin RecyclerView"),
            Note(4, "Reminder", "Call my friend tonight")
        )

        // RecyclerView setup
        adapter = NoteAdapter(noteList)

        binding.recyclerNotes.layoutManager = LinearLayoutManager(this)
        binding.recyclerNotes.adapter = adapter
    }
}