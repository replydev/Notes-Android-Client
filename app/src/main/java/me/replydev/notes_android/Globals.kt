package me.replydev.notes_android

import com.google.gson.Gson
import me.replydev.notes_android.crypto.PyXChaCha20Instance
import me.replydev.notes_android.json.Note
import me.replydev.notes_android.net.SimpleSocket
import kotlin.properties.Delegates

class Globals {
    companion object{
        val gson = Gson()
        lateinit var socket: SimpleSocket
        lateinit var password: String
        lateinit var pyCryptoForServerCommunications: PyXChaCha20Instance
        var userId by Delegates.notNull<Int>()
    }
}