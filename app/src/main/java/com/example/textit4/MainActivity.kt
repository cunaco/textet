package com.example.textit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var noteTitle: EditText
    private lateinit var noteText: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonShare: Button
    private lateinit var buttonDelete: Button
    private lateinit var noteListView: ListView

    private val noteList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    // Переменная для хранения выделенной заметки
    private var selectedNoteIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Связываем элементы интерфейса
        noteTitle = findViewById(R.id.noteTitle)
        noteText = findViewById(R.id.noteText)
        buttonSave = findViewById(R.id.buttonSave)
        buttonShare = findViewById(R.id.buttonShare)
        buttonDelete = findViewById(R.id.buttonDelete)
        noteListView = findViewById(R.id.noteListView)

        // Инициализируем адаптер для ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, noteList)
        noteListView.adapter = adapter

        // Загружаем сохраненные заметки
        loadNotes()

        // Логика кнопки "Сохранить"
        buttonSave.setOnClickListener {
            val title = noteTitle.text.toString().trim()
            val note = noteText.text.toString().trim()

            if (title.isNotEmpty() && note.isNotEmpty()) {
                val noteItem = "$title: $note"
                noteList.add(noteItem)
                adapter.notifyDataSetChanged()
                saveNotes() // Сохраняем заметки
                noteTitle.setText("") // Очищаем поле после сохранения
                noteText.setText("")  // Очищаем поле после сохранения
            } else {
                Toast.makeText(this, "Заполните оба поля", Toast.LENGTH_SHORT).show()
            }
        }

        // Логика кнопки "Поделиться" для выделенной заметки
        buttonShare.setOnClickListener {
            selectedNoteIndex?.let { index ->
                val selectedNote = noteList[index]
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, selectedNote)
                startActivity(Intent.createChooser(shareIntent, "Поделиться заметкой"))
            } ?: run {
                Toast.makeText(this, "Выберите заметку", Toast.LENGTH_SHORT).show()
            }
        }

        // Логика кнопки "Удалить" для выделенной заметки
        buttonDelete.setOnClickListener {
            selectedNoteIndex?.let { index ->
                noteList.removeAt(index)
                adapter.notifyDataSetChanged()
                saveNotes() // Сохраняем после удаления
                selectedNoteIndex = null // Сбрасываем выделение
            } ?: run {
                Toast.makeText(this, "Выберите заметку", Toast.LENGTH_SHORT).show()
            }
        }

        // Выбор заметки при клике (выделение заметки)
        noteListView.setOnItemClickListener { _, view, position, _ ->
            // Если уже есть выделенная заметка, отменяем её выделение
            if (selectedNoteIndex != null) {
                noteListView.getChildAt(selectedNoteIndex!!).setBackgroundColor(0x00000000) // Сброс цвета выделения
            }

            // Выделяем выбранную заметку
            selectedNoteIndex = position
            view.setBackgroundColor(0xFFE0E0E0.toInt()) // Цвет выделенной заметки
        }
    }

    // Сохранение заметок в SharedPreferences
    private fun saveNotes() {
        val sharedPreferences = getSharedPreferences("notes_pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonArray = JSONArray(noteList)
        editor.putString("notes", jsonArray.toString())
        editor.apply()
    }

    // Загрузка заметок из SharedPreferences
    private fun loadNotes() {
        val sharedPreferences = getSharedPreferences("notes_pref", Context.MODE_PRIVATE)
        val notesString = sharedPreferences.getString("notes", null)
        if (notesString != null) {
            val jsonArray = JSONArray(notesString)
            for (i in 0 until jsonArray.length()) {
                noteList.add(jsonArray.getString(i))
            }
            adapter.notifyDataSetChanged()
        }
    }
}
