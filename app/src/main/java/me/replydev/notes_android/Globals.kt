package me.replydev.notes_android

import com.google.gson.Gson
import me.replydev.notes_android.crypto.PyXChaCha20Instance
import me.replydev.notes_android.net.SimpleEncryptedSocket
import kotlin.properties.Delegates

class Globals {
    companion object{
        val gson = Gson()
        lateinit var encryptedSocket: SimpleEncryptedSocket
        lateinit var password: String
        var userId by Delegates.notNull<Int>()
    }
}