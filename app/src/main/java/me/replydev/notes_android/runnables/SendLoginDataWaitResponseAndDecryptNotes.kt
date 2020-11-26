package me.replydev.notes_android.runnables

import me.replydev.notes_android.Globals
import java.util.concurrent.Callable

class SendLoginDataWaitResponseAndDecryptNotes(private val userPassJsonData: String) : Callable<ArrayList<String>> {
    @Throws(Exception::class)
    override fun call(): ArrayList<String> {
        Globals.encryptedSocket.send(userPassJsonData)
        Globals.userId = Globals.encryptedSocket.read().toInt()
        Globals.encryptedSocket.send("")

        var messageFromServer = Globals.encryptedSocket.read()
        val encryptedNotes = ArrayList<String>()

        while(messageFromServer != "/end/"){
            encryptedNotes.add(messageFromServer)
            Globals.encryptedSocket.send("")
            messageFromServer = Globals.encryptedSocket.read()
        }
        Globals.encryptedSocket.send("")

        return encryptedNotes
    }
}