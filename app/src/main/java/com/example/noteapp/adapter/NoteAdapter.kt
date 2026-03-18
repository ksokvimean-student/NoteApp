package com.example.noteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.databinding.ItemNoteBinding
import com.example.noteapp.model.Note
import java.util.Locale

class NoteAdapter(
    private var notes: List<Note>,
    private val onNoteClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var allNotes: List<Note> = notes
    class NoteViewHolder(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        holder.binding.tvTitle.text = note.title
        holder.binding.tvContent.text = note.content

        holder.binding.root.setOnClickListener {
            onNoteClick(note)
        }

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(note)
        }
    }

    override fun getItemCount(): Int = notes.size

    fun updateNotes(newNotes: List<Note>) {
        this.notes = newNotes
        this.allNotes = newNotes
        notifyDataSetChanged()
    }

    // handles the search logic
    fun filter(query: String): Int {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())

        notes = if (lowerCaseQuery.isEmpty()) {
            allNotes
        } else {
            allNotes.filter { note ->
                note.title.lowercase(Locale.getDefault()).contains(lowerCaseQuery)
            }
        }
        notifyDataSetChanged()
        return notes.size
    }
}