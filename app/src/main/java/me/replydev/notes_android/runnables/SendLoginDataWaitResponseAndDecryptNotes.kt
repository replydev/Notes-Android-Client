package me.replydev.notes_android.runnables

import me.replydev.notes_android.Globals
import java.util.concurrent.Callable

class SendLoginDataWaitResponseAndDecryptNotes(private val userPassJsonData: String) : Callable<String> {
    @Throws(Exception::class)
    override fun call(): String {
        Globals.encryptedSocket.send(userPassJsonData)
        Globals.userId = Globals.encryptedSocket.read().toInt()
        Globals.encryptedSocket.send("")
        return Globals.encryptedSocket.read()
    }
}