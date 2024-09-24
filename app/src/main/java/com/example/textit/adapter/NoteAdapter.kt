package com.example.textet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.textet.MainActivity
import com.example.textet.R
import com.example.textet.model.Note

class NoteAdapter(
    private var noteList: List<Note>,
    private val activity: MainActivity
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var selectedNote: Note? = null

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteText: TextView = itemView.findViewById(R.id.noteText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.noteText.text = note.text

        holder.itemView.setOnClickListener {
            selectedNote = note
            notifyDataSetChanged()
        }

        if (note == selectedNote) {
            holder.itemView.setBackgroundResource(R.color.selected_note_background)
        } else {
            holder.itemView.setBackgroundResource(android.R.color.transparent)
        }
    }

    override fun getItemCount(): Int = noteList.size

    // Обновление списка заметок
    fun updateList(newList: List<Note>) {
        noteList = newList
        notifyDataSetChanged()
    }

    // Получение выбранной заметки
    fun getSelectedNote(): Note? {
        return selectedNote
    }
}
