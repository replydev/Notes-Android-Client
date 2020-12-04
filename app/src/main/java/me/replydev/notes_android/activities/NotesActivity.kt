package me.replydev.notes_android.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import me.replydev.notes_android.Globals
import me.replydev.notes_android.R
import me.replydev.notes_android.activities.adapters.NotesAdapter
import me.replydev.notes_android.json.EncryptedNote
import me.replydev.notes_android.json.Note
import me.replydev.notes_android.runnables.DecryptNotes
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.properties.Delegates


class NotesActivity : AppCompatActivity(){
    private lateinit var notesAdapter: NotesAdapter
    private var userId by Delegates.notNull<Int>()
    private lateinit var notes: ArrayList<Note>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        userId = intent.getIntExtra("USER_ID", -1)
        val encryptedNotesJson = intent.getStringExtra("ENCRYPTED_NOTES")
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val encryptedNotesListJson: ArrayList<String> = Globals.gson.fromJson(
            encryptedNotesJson,
            ArrayList<String>().javaClass
        )
        val encryptedNotes: ArrayList<EncryptedNote> = getEncryptedNotes(encryptedNotesListJson)
        notes = ArrayList() //already initialized for containing notes
        notesAdapter = NotesAdapter(
            applicationContext,
            R.layout.note_list_item,
            notes
        )
        val notesListView = findViewById<ListView>(R.id.notesListView)
        notesListView.adapter = notesAdapter

        notesListView.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val note: Note = adapterView.getItemAtPosition(i) as Note
            val intent = Intent(this, WriteActivity::class.java).apply {
                putExtra("ID", note.id)
                putExtra("JSON", note.toJson())
            }
            startActivityForResult(intent, 1)
            //println(note)
        }
        executorService.execute(
            DecryptNotes(
                encryptedNotes,
                Globals.password,
                notes
            )
        )
        notesAdapter.notifyDataSetChanged()
    }

    private fun getEncryptedNotes(encryptedNotesListJson: ArrayList<String>): ArrayList<EncryptedNote> {
        val encryptedNotes = ArrayList<EncryptedNote>()
        for(s in encryptedNotesListJson){
            val temp: EncryptedNote = Globals.gson.fromJson(s, EncryptedNote::class.java)
            encryptedNotes.add(temp)
        }
        return encryptedNotes
    }

    fun addNewNote(view: View){
        val lastId = if(notes.size == 0){
            0
        } else{
            notes[notes.size - 1].id + 1
        }
        val intent = Intent(this, WriteActivity::class.java).apply {
            putExtra("ID", lastId)
            putExtra("USER_ID", userId)
        }
        startActivityForResult(intent, 1)
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == 1) { //add new note
            val noteJson = data?.getStringExtra("NOTE") ?: return //return if data?.getStringExtra("NOTE") is null
            val note: Note = Globals.gson.fromJson(noteJson, Note::class.java)
            notes.add(note)
        }
        else if(resultCode == 2){  //update existing note
            val noteJson = data?.getStringExtra("NOTE") ?: return //return if data?.getStringExtra("NOTE") is null
            val note: Note = Globals.gson.fromJson(noteJson, Note::class.java)
            for (i in 0 until notes.size) {
                if(note.id == notes[i].id){
                    notes[i] = note
                    break
                }
            }
        }
        notesAdapter.notifyDataSetChanged()
    }
}