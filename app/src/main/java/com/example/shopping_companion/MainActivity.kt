package com.example.shopping_companion

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


/*
Note Taking Project
Authors: Kenzie Evan and Kenneth Nguyen
Mobile Application Security CSCI 445

*/

class MainActivity : AppCompatActivity() {

    // Note Variables
//    var notes = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ListView of notes
        var notesList = findViewById<ListView>(R.id.notesList)



        // Using Shared Preferences for local storage of notes
        val sharedPreferences = applicationContext.getSharedPreferences("NotesApplication", MODE_PRIVATE)
        val set = sharedPreferences.getStringSet("notes", null) as HashSet<String>?

        if(set == null)
        {
            notes.add("Example Note")
        }
        else
        {
            notes = ArrayList(set)
        }


        // Initialize ListView Adapter
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, notes)
        notesList.adapter = arrayAdapter


        // Method to move to Note Editor Activity by clicking on ListView
        notesList.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            val intent = Intent(
                applicationContext,
                NoteEditingActivity::class.java
            )

            intent.putExtra("noteID", position) // to tell us which row of listView was tapped

            startActivity(intent)
        }

        // Code to Enable Deletion. Prompt by long clicking
        notesList.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            AlertDialog.Builder(this@MainActivity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete?")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    notes.removeAt(position)
                    arrayAdapter!!.notifyDataSetChanged()

                    // Updates the local storage after deleting
                    val sharedPreferences = applicationContext.getSharedPreferences("NotesApplication", MODE_PRIVATE)
                    val set = HashSet(notes)
                    sharedPreferences.edit().putStringSet("notes", set).apply()
                })
                .setNegativeButton("No", null)
                .show()
            true
        }

        // Create New Note Button Implementation
        var addNoteButton = findViewById<Button>(R.id.addNoteButton)

        addNoteButton.setOnClickListener { //
            val intent = Intent(
                applicationContext,
                NoteEditingActivity::class.java
            )
            startActivity(intent)
        }

        arrayAdapter!!.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        // Make sure the array adapter edits. Possibly could raise issue if main activity is not resumed
        arrayAdapter!!.notifyDataSetChanged()
    }


    /*
    Option Menu Handling Section
    Give ability to create notes from the menu
    */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == R.id.add_note) {
            val intent = Intent(
                applicationContext,
                NoteEditingActivity::class.java
            )
            startActivity(intent)
            return true
        }
        return false
    }

    // Static Variables that can be used anywhere
    companion object {
        var arrayAdapter: ArrayAdapter<String>? = null
        var notes = ArrayList<String>()
    }


}