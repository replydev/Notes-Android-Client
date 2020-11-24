package me.replydev.notes_android.runnables

import me.replydev.notes_android.Globals
import me.replydev.notes_android.net.SimpleSocket
import java.util.concurrent.Callable

class SendLoginDataWaitResponseAndDecryptNotes(private val s: SimpleSocket, private val encryptedData: String) : Callable<String> {
    @Throws(Exception::class)
    override fun call(): String {
        s.send(encryptedData)

        val encryptedUserId = s.read()

        Globals.userId = Globals.pyCryptoForServerCommunications.decrypt(encryptedUserId).toInt()
        s.send("")

        val responseEncrypted: String = s.read()
        val response = Globals.pyCryptoForServerCommunications.decrypt(responseEncrypted)
        return response
    }
}