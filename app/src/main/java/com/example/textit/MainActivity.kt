package com.example.textet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textit.adapter.NoteAdapter
import com.example.textit.model.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: MutableList<Note>
    private lateinit var searchText: EditText
    private lateinit var noteTextInput: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonShare: Button
    private lateinit var buttonDelete: Button
    private lateinit var recyclerView: RecyclerView

    private val sharedPrefFile = "com.example.textit.notes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Загружаем заметки из SharedPreferences
        noteList = loadNotes()

        noteAdapter = NoteAdapter(noteList)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter

        searchText = findViewById(R.id.searchText)
        noteTextInput = findViewById(R.id.noteText)
        buttonSave = findViewById(R.id.buttonSave)
        buttonShare = findViewById(R.id.buttonShare)
        buttonDelete = findViewById(R.id.buttonDelete)

        // Логика сохранения заметки
        buttonSave.setOnClickListener {
            val content = noteTextInput.text.toString()
            if (content.isNotEmpty()) {
                val newNote = Note("Заметка", content)
                noteList.add(newNote)
                noteAdapter.updateList(noteList)
                saveNotes() // Сохраняем заметки при добавлении новой
                noteTextInput.text.clear()
            }
        }

        // Логика для кнопки "Поделиться"
        buttonShare.setOnClickListener {
            val content = noteTextInput.text.toString()
            if (content.isNotEmpty()) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, content)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, "Поделиться заметкой"))
            }
        }

        // Логика для кнопки "Удалить"
        buttonDelete.setOnClickListener {
            val content = noteTextInput.text.toString()
            noteList.removeIf { it.content == content }
            noteAdapter.updateList(noteList)
            saveNotes() // Сохраняем изменения после удаления
        }

        // Логика для поиска заметок
        searchText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = noteList.filter { it.content.contains(s.toString(), true) }
                noteAdapter.updateList(filteredList)
            }
        })
    }

    // Метод для сохранения заметок в SharedPreferences
    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(noteList)
        editor.putString("notes", json)
        editor.apply()
    }

    // Метод для загрузки заметок из SharedPreferences
    private fun loadNotes(): MutableList<Note> {
        val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("notes", null)
        val type = object : TypeToken<MutableList<Note>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
