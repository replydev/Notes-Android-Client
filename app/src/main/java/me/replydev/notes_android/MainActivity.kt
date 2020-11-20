package me.replydev.notes_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import java.net.Socket

class MainActivity : AppCompatActivity() {

    private var pythonInstance: Python? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Python.start(AndroidPlatform(this))

        setContentView(R.layout.activity_main)
        pythonInstance = Python.getInstance()
    }

    fun connectButton(view: View){
        Thread {
            val editText = findViewById<EditText>(R.id.ipAddressText)
            val ipAddress = editText.text.toString()
            val s = Socket(ipAddress, 50000)
            val pyDiffieHellmanInstance = PyDiffieHellmanInstance(ipAddress)
            val sharedKey = pyDiffieHellmanInstance.getSharedKey()
            Looper.prepare()
            Toast.makeText(this, sharedKey.toString(), 4)
            s.close()
        }.start()
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