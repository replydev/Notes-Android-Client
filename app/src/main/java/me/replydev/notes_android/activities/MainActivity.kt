package me.replydev.notes_android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import me.replydev.notes_android.Globals
import me.replydev.notes_android.R
import me.replydev.notes_android.runnables.CloseConnection
import me.replydev.notes_android.runnables.ConnectToServer
import me.replydev.notes_android.runnables.ExchangeKey
import me.replydev.notes_android.runnables.SendLoginDataWaitResponseAndDecryptNotes
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.collections.HashMap
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(){
    private lateinit var ipAddress: String
    private var firstTime by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Python.start(AndroidPlatform(this))
        ipAddress = "192.168.1.124"
        firstTime = true
    }

    override fun onResume() {
        super.onResume()
        if(firstTime){
            connectToServer()
            firstTime = false
        }
        else{
            val executorService: ExecutorService = Executors.newSingleThreadExecutor()
            executorService.submit(CloseConnection())
        }
    }

    fun loginButton(view: View){
        val usernameText = findViewById<EditText>(R.id.usernameText)
        val passwordText = findViewById<EditText>(R.id.passwordText)

        val map = HashMap<String,String>()

        map["username"] = usernameText.text.toString()
        map["password"] = passwordText.text.toString()
        Globals.password = passwordText.text.toString()

        val jsonToSend = Globals.gson.toJson(map)
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val futureText: Future<ArrayList<String>> = executorService.submit(SendLoginDataWaitResponseAndDecryptNotes(jsonToSend))

        val encryptedNotes = futureText.get() //if the user is logged in the server sends encrypted notes

        /*if (encryptedNotes == "no"){
            println("Not logged in")
            exitProcess(-1)
        }
        else println("Logged in")*/

        decryptNotes(encryptedNotes)
    }

    private fun decryptNotes(encryptedNotes: ArrayList<String>){
        val encryptedNotesJson = Globals.gson.toJson(encryptedNotes)
        val intent = Intent(this, NotesActivity::class.java).apply {
            putExtra("ENCRYPTED_NOTES", encryptedNotesJson)
            putExtra("PASSWORD", Globals.password)
        }
        startActivity(intent)
    }

    private fun connectToServer(){
        val executorService = Executors.newSingleThreadExecutor()
        val simpleSocketFuture = executorService.submit(ConnectToServer(ipAddress))
        Globals.encryptedSocket = simpleSocketFuture.get()
        val pyCryptoFuture = executorService.submit(ExchangeKey(ipAddress))
        Globals.encryptedSocket.setEncryptionInstance(pyCryptoFuture.get())
    }
}