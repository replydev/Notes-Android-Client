package me.replydev.notes_android.json

import me.replydev.notes_android.Globals

class Note(var id: Int, var authorId: Int, var salt: String,var title: String, var body: String){
    fun toJson(): String{
        return Globals.gson.toJson(this)
    }
}