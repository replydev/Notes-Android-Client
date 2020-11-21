package me.replydev.notes_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.gson.Gson
import me.replydev.notes_android.Connection
import me.replydev.notes_android.EncryptedNote
import me.replydev.notes_android.crypto.PyDiffieHellmanInstance
import me.replydev.notes_android.R
import me.replydev.notes_android.SimpleSocket
import me.replydev.notes_android.crypto.PyXChaCha20Instance
import me.replydev.notes_android.runnables.ConnectToServer
import me.replydev.notes_android.runnables.ExchangeKey
import me.replydev.notes_android.runnables.SendLoginDataAndWaitResponse
import java.net.ConnectException
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.collections.HashMap
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {


    private lateinit var ipAddress: String
    private lateinit var s: SimpleSocket
    //private lateinit var sharedKeyForServerCommunications: String
    private lateinit var pyCryptoForServerCommunications: PyXChaCha20Instance
    private lateinit var connection: Connection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        ipAddress = intent.getStringExtra("IP_ADDRESS").toString()
        connectToServer()
    }

    fun loginButton(view: View){
        val usernameText = findViewById<EditText>(R.id.usernameText)
        val passwordText = findViewById<EditText>(R.id.passwordText)

        val map = HashMap<String,String>()

        map["username"] = usernameText.text.toString()
        map["password"] = passwordText.text.toString()

        val gson = Gson()
        val jsonToSend = gson.toJson(map)

        val encryptedJsonSafeToSend = pyCryptoForServerCommunications.encrypt(jsonToSend)
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val futureText: Future<String> = executorService.submit(SendLoginDataAndWaitResponse(s,encryptedJsonSafeToSend))

        val encryptedNotes = futureText.get() //if the user is logged in the server sends encrypted notes

        if (encryptedNotes == "no"){
            println("Not logged in")
            exitProcess(-1)
        }
        else println("Logged in")

        decryptNotes(encryptedNotes)
    }

    private fun decryptNotes(encryptedNotesFromServerButWeHaveStillToDecryptThem: String){
        val gson = Gson()
        val encryptedNotesJson = pyCryptoForServerCommunications.decrypt(encryptedNotesFromServerButWeHaveStillToDecryptThem)
        val encryptedNotes: Vector<EncryptedNote> = gson.fromJson(encryptedNotesJson, Vector<EncryptedNote>().javaClass)
        println(encryptedNotes)
    }

    private fun connectToServer(){
        val executorService = Executors.newSingleThreadExecutor()
        val simpleSocketFuture = executorService.submit(ConnectToServer(ipAddress))
        s = simpleSocketFuture.get()
        val pyCryptoFuture = executorService.submit(ExchangeKey(ipAddress))
        pyCryptoForServerCommunications = pyCryptoFuture.get()
        if(!this::s.isInitialized){
            println("Socket not initialized")
            exitProcess(-1)
        }
        //connection = Connection(s,sharedKeyForServerCommunications)
    }
}