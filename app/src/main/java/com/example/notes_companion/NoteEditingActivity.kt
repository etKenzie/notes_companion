package com.example.notes_companion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.notes_companion.MainActivity.Companion.arrayAdapter
import com.example.notes_companion.MainActivity.Companion.notes


class NoteEditingActivity : AppCompatActivity() {

    // Var to keep track of current page
    var noteID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editing)

        // Initialize note page
        val editText = findViewById<View>(R.id.notePage) as EditText
        val intent = intent

        // Variables from Main Activity
        noteID = intent.getIntExtra("noteID", -1) // So if empty it goes to new Note

        // Get content of notes in String ArrayList
        if(noteID != -1)
        {
            if (notes != null) {
                editText.setText(notes[noteID])
            };
        }
        else
        {
            if (notes != null) {
                // Process of Creating a new Note if the ID does not exist
                notes.add("")
                noteID = notes.size -1
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (notes != null) {
                    notes[noteID] = s.toString()
                    arrayAdapter!!.notifyDataSetChanged()

                    // Stores the notes we have changed
                    val sharedPreferences = applicationContext.getSharedPreferences("NotesApplication", MODE_PRIVATE)
                    val set = HashSet(notes)
                    sharedPreferences.edit().putStringSet("notes", set).apply()

                }

            }

            override fun afterTextChanged(s: Editable) {}
        })
    }
}