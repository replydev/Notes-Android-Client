package me.replydev.notes_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import me.replydev.notes_android.Globals
import me.replydev.notes_android.R
import me.replydev.notes_android.json.Note
import me.replydev.notes_android.runnables.SendNoteToServer
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.Executors
import kotlin.properties.Delegates

class AddNoteActivity : AppCompatActivity() {

    private var id by Delegates.notNull<Int>()
    private var userId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        id = intent.getIntExtra("ID",-1)
        userId = intent.getIntExtra("USER_ID",-1)
    }

    fun saveNewNote(view: View){
        val titleAddNoteEditText = findViewById<EditText>(R.id.titleAddNoteEditText)
        val bodyAddNoteEditText = findViewById<EditText>(R.id.bodyAddNoteEditText)
        val randomSaltBytes = ByteArray(32)
        SecureRandom().nextBytes(randomSaltBytes)
        val saltBase64 = Base64.getEncoder().encodeToString(randomSaltBytes)
        val note = Note(id,userId,saltBase64,titleAddNoteEditText.text.toString(),bodyAddNoteEditText.text.toString())
        Globals.notes.add(note)
        val executorService = Executors.newSingleThreadExecutor()
        executorService.submit(SendNoteToServer(note))
        finish()
    }
}