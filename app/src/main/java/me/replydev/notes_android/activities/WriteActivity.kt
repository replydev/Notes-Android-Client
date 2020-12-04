package me.replydev.notes_android.activities

import android.content.Intent
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

class WriteActivity : AppCompatActivity() {

    private var id by Delegates.notNull<Int>()
    private var userId by Delegates.notNull<Int>()
    private lateinit var noteJson: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        id = intent.getIntExtra("ID",-1)
        userId = intent.getIntExtra("USER_ID",-1)
        noteJson = intent.getStringExtra("JSON").toString()
        if(noteJson != "null"){  //update existing note
            val note = Globals.gson.fromJson(noteJson,Note::class.java)
            val titleAddNoteEditText = findViewById<EditText>(R.id.titleAddNoteEditText)
            val bodyAddNoteEditText = findViewById<EditText>(R.id.bodyAddNoteEditText)
            titleAddNoteEditText.setText(note.title)
            bodyAddNoteEditText.setText(note.body)
            id = note.id
            userId = note.authorId
        }
    }

    fun executeOperation(view: View){
        if(noteJson == "null"){
            buildNoteAndSend(1)
        }
        else{
            buildNoteAndSend(2)
        }
    }

    /*private fun updateNote(){
        val titleAddNoteEditText = findViewById<EditText>(R.id.titleAddNoteEditText)
        val bodyAddNoteEditText = findViewById<EditText>(R.id.bodyAddNoteEditText)
        val randomSaltBytes = ByteArray(32)
        SecureRandom().nextBytes(randomSaltBytes)
        val saltBase64 = Base64.getEncoder().encodeToString(randomSaltBytes)
        val note =  Note(id,userId,saltBase64,titleAddNoteEditText.text.toString(),bodyAddNoteEditText.text.toString())
        val resultCode = 2
        val resultIntent = Intent()
        resultIntent.putExtra("NOTE", note.toJson())
        setResult(resultCode, resultIntent)
        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(SendNoteToServer(note))
        finish()
    }*/

    private fun buildNoteAndSend(resultId: Int){
        val titleAddNoteEditText = findViewById<EditText>(R.id.titleAddNoteEditText)
        val bodyAddNoteEditText = findViewById<EditText>(R.id.bodyAddNoteEditText)
        val randomSaltBytes = ByteArray(32)
        SecureRandom().nextBytes(randomSaltBytes)
        val saltBase64 = Base64.getEncoder().encodeToString(randomSaltBytes)
        val note = Note(id,userId,saltBase64,titleAddNoteEditText.text.toString(),bodyAddNoteEditText.text.toString())
        val resultIntent = Intent()
        resultIntent.putExtra("NOTE", note.toJson())
        setResult(resultId, resultIntent)
        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(SendNoteToServer(note))
        finish()
    }
}