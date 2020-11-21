package me.replydev.notes_android.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import me.replydev.notes_android.R

class MainActivity : AppCompatActivity() {

    private lateinit var pythonInstance: Python
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Python.start(AndroidPlatform(this))

        setContentView(R.layout.activity_main)
        pythonInstance = Python.getInstance()
    }

    fun connectButton(view: View){
        val editText = findViewById<EditText>(R.id.ipAddressText)
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra("IP_ADDRESS", editText.text.toString())
        }
        startActivity(intent)
    }

    /*fun startButton(view: View){
        val editText = findViewById<EditText>(R.id.editTextTextPersonName)
        val textView = findViewById<TextView>(R.id.textView)
        val cryptoModule = pythonInstance?.getModule("MyCryptograpy.XChaCha20Py")
        val cryptoObject = cryptoModule?.callAttr("XChaCha20Crypto",editText.text.toString())
        val json = cryptoObject?.callAttr("encrypt","plain text")
        textView.text = json.toString()
    }*/
}