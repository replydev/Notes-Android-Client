package me.replydev.notes_android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import me.replydev.notes_android.Globals
import me.replydev.notes_android.R
import me.replydev.notes_android.runnables.CloseConnection
import me.replydev.notes_android.runnables.ConnectToServer
import me.replydev.notes_android.runnables.ExchangeKey
import me.replydev.notes_android.runnables.SendLoginDataWaitResponseAndDecryptNotes
import me.replydev.notes_android.utils.PasswordChecker
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
        val connectButton = findViewById<Button>(R.id.connectButton)
        if(firstTime){
            connectToServer()
            firstTime = false
            connectButton.isEnabled = false
        }
        else{
            val executorService: ExecutorService = Executors.newSingleThreadExecutor()
            executorService.execute(CloseConnection())
            val loginButton = findViewById<Button>(R.id.loginButton)
            runOnUiThread{
                loginButton.isEnabled = false
                connectButton.isEnabled = true
            }
        }
    }

    fun loginButton(view: View){
        val statusTextView = findViewById<TextView>(R.id.statusTextView)
        runOnUiThread{
            statusTextView.text = "Login..."
        }
        val usernameText = findViewById<EditText>(R.id.usernameText)
        val passwordText = findViewById<EditText>(R.id.passwordText)

        val username = usernameText.text.toString()
        val password = passwordText.text.toString()

        if (!goodPassword(password)){
            runOnUiThread{
                statusTextView.text = "Bad password, insert at least 1 Upper char, 2 number char and 1 special char, with a minimum length of 8"
            }
            return
        }

        val map = HashMap<String,String>()

        map["username"] = username
        map["password"] = password
        Globals.password = password
        val jsonToSend = Globals.gson.toJson(map)
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        val futureText: Future<ArrayList<String>> = executorService.submit(SendLoginDataWaitResponseAndDecryptNotes(jsonToSend))
        val encryptedNotes  = futureText.get() //if the user is logged in the server sends encrypted notes
        if (encryptedNotes == null){
            executorService.execute(CloseConnection())
            runOnUiThread{
                statusTextView.text = "Invalid user or password"
                val loginButton = findViewById<Button>(R.id.loginButton)
                val connectButton = findViewById<Button>(R.id.connectButton)
                loginButton.isEnabled = false
                connectButton.isEnabled = true
            }
            return
        }
        decryptNotes(encryptedNotes)
    }

    private fun goodPassword(password: String): Boolean {
        return PasswordChecker.checkPwd(password)
    }

    private fun decryptNotes(encryptedNotes: ArrayList<String>){
        val encryptedNotesJson = Globals.gson.toJson(encryptedNotes)
        val intent = Intent(this, NotesActivity::class.java).apply {
            putExtra("ENCRYPTED_NOTES", encryptedNotesJson)
            putExtra("PASSWORD", Globals.password)
        }
        startActivity(intent)
    }

    fun connectToServer(view: View){
        connectToServer()
    }

    private fun connectToServer(){
        Thread{
            val loginButton = findViewById<Button>(R.id.loginButton)
            val statusTextView = findViewById<TextView>(R.id.statusTextView)
            runOnUiThread{
                loginButton.isEnabled = false //block login button until we get connected
                statusTextView.text = "Connecting to $ipAddress"
            }
            val executorService = Executors.newSingleThreadExecutor()
            val simpleSocketFuture = executorService.submit(ConnectToServer(ipAddress))
            Globals.encryptedSocket = simpleSocketFuture.get()
            val pyCryptoFuture = executorService.submit(ExchangeKey(ipAddress))
            Globals.encryptedSocket.setEncryptionInstance(pyCryptoFuture.get())
            runOnUiThread{
                loginButton.isEnabled = true
                statusTextView.text = "Connected!"
            }
        }.start()
    }
}