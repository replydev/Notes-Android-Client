package me.replydev.notes_android

import me.replydev.notes_android.crypto.PyXChaCha20Instance
import java.net.Socket

class Connection(s: Socket,key: String) {
    private val socket = s
    private val pyXChaCha20Instance = PyXChaCha20Instance(key)
}