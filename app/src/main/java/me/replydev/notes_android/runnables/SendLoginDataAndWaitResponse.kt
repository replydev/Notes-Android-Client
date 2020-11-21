package me.replydev.notes_android.runnables

import me.replydev.notes_android.SimpleSocket
import java.util.concurrent.Callable

class SendLoginDataAndWaitResponse(private val s: SimpleSocket, private val encryptedData: String) : Callable<String> {
    @Throws(Exception::class)
    override fun call(): String {
        s.send(encryptedData)
        val response: String = s.read()
        return response
    }
}