package com.examples.notesapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.examples.notesapp.databinding.ActivityMainScreenBinding

class MainScreen : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var db: NotesDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private var notesList: List<Note> = ArrayList()
    private lateinit var emptyView: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = NotesDatabaseHelper(this)
        notesAdapter = NotesAdapter(db.getAllNotes(), this)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        // Set up search functionality
        val notesSearch = binding.notesSearch
        notesSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                filterNotes(s.toString())
            }
        })

        binding.addbutton.setOnClickListener {
            val intent = Intent(this, AddNotes::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
    }

    private fun filterNotes(query: String) {
        val filteredNotes = ArrayList<Note>()
        for (note in notesList) {
            if (note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)) {
                filteredNotes.add(note)
            }
        }
        notesAdapter.filterList(filteredNotes)

        if (filteredNotes.isEmpty()) {
            // Show empty view
            binding.notesRecyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            // Hide empty view
            binding.notesRecyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }
}
