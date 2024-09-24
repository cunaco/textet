package com.example.textet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textet.adapter.NoteAdapter
import com.example.textet.model.Note

class MainActivity : AppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var searchText: EditText
    private lateinit var noteTextInput: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonShare: Button
    private lateinit var buttonDelete: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация списка заметок
        noteList = mutableListOf()

        // Инициализация адаптера и RecyclerView
        noteAdapter = NoteAdapter(noteList, this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter

        // Инициализация полей ввода и кнопок
        noteTextInput = findViewById(R.id.noteText)
        buttonSave = findViewById(R.id.buttonSave)
        buttonShare = findViewById(R.id.buttonShare)
        buttonDelete = findViewById(R.id.buttonDelete)
        searchText = findViewById(R.id.searchText)

        // Логика для сохранения заметки
        buttonSave.setOnClickListener {
            val text = noteTextInput.text.toString().trim()

            if (text.isNotEmpty()) {
                val newNote = Note(text)
                noteList.add(newNote)
                noteAdapter.updateList(noteList)
                noteTextInput.text.clear()
                Toast.makeText(this, "Заметка сохранена", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Введите текст заметки", Toast.LENGTH_SHORT).show()
            }
        }

        // Логика для отправки заметки
        buttonShare.setOnClickListener {
            val selectedNote = noteAdapter.getSelectedNote()

            if (selectedNote != null) {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, selectedNote.text)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(sendIntent, "Поделиться заметкой"))
            } else {
                Toast.makeText(this, "Выберите заметку для отправки", Toast.LENGTH_SHORT).show()
            }
        }

        // Логика для удаления заметки
        buttonDelete.setOnClickListener {
            val selectedNote = noteAdapter.getSelectedNote()

            if (selectedNote != null) {
                noteList.remove(selectedNote)
                noteAdapter.updateList(noteList)
                Toast.makeText(this, "Заметка удалена", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Выберите заметку для удаления", Toast.LENGTH_SHORT).show()
            }
        }

        // Логика поиска
        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotes(s.toString())
            }
        })
    }

    // Метод для фильтрации заметок
    private fun filterNotes(query: String) {
        val filteredList = noteList.filter {
            it.text.contains(query, ignoreCase = true)
        }
        noteAdapter.updateList(filteredList)
    }
}
