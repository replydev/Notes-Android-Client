package me.replydev.notes_android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import me.replydev.notes_android.Globals
import me.replydev.notes_android.json.EncryptedNote
import me.replydev.notes_android.R
import me.replydev.notes_android.activities.adapters.NotesAdapter
import me.replydev.notes_android.crypto.DecryptNotes
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class NotesActivity : AppCompatActivity() {

    private lateinit var notesAdapter: NotesAdapter

    private var userId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        notesAdapter = NotesAdapter(
                applicationContext,
                R.layout.row_view,
                Globals.notes
        )

        userId = intent.getIntExtra("USER_ID",-1)

        val encryptedNotesJson = intent.getStringExtra("ENCRYPTED_NOTES")
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()

        val encryptedNotesListJson: ArrayList<String> = Globals.gson.fromJson(encryptedNotesJson,ArrayList<String>().javaClass)

        val encryptedNotes: ArrayList<EncryptedNote> = getEncryptedNotes(encryptedNotesListJson)

        val futureNotes = executorService.submit(DecryptNotes(encryptedNotes,Globals.password))

        Globals.notes = futureNotes.get()

        val notesListView = findViewById<ListView>(R.id.notesListView)

        notesListView.adapter = notesAdapter
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
        val lastId = if(Globals.notes.size == 0){
            0
        } else{
            Globals.notes[Globals.notes.size - 1].id
        }
        val intent = Intent(this, AddNoteActivity::class.java).apply {
            putExtra("ID", lastId)
            putExtra("USER_ID",userId)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.notifyDataSetChanged()
    }
}