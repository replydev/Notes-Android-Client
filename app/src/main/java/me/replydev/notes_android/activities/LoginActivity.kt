package me.replydev.notes_android.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.gson.Gson
import me.replydev.notes_android.Connection
import me.replydev.notes_android.crypto.PyDiffieHellmanInstance
import me.replydev.notes_android.R
import me.replydev.notes_android.crypto.PyXChaCha20Instance
import me.replydev.notes_android.runnables.SendLoginData
import java.net.ConnectException
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {


    private lateinit var ipAddress: String
    private lateinit var s: Socket
    private lateinit var sharedKeyForServerCommunications: String
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
        val pyXChaCha20Instance = PyXChaCha20Instance(sharedKeyForServerCommunications)
        val encryptedJsonSafeToSend = pyXChaCha20Instance.encrypt(jsonToSend)
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val futureText: Future<String> = executorService.submit(SendLoginData(s,encryptedJsonSafeToSend))

        val text = futureText.get()

        if (text == "no"){
            Toast.makeText(this,"Not logged in",4)
            exitProcess(-1)
        }
        else println("Logged in")
    }


    private fun connectToServer(){

        val t = Thread {
            try {
                s = Socket(ipAddress, 50000)
                val pyDiffieHellmanInstance = PyDiffieHellmanInstance(ipAddress)
                sharedKeyForServerCommunications = pyDiffieHellmanInstance.getSharedKey().toString()
            } catch (e: ConnectException) {
                e.printStackTrace()
                //TODO Make visual error
            }
        }
        t.start()
        t.join()  //can insert millis

        if(!this::s.isInitialized){
            println("Socket not initialized")
            exitProcess(-1)
        }
        connection = Connection(s,sharedKeyForServerCommunications)
    }
}