package me.replydev.notes_android

import com.chaquo.python.PyObject
import com.chaquo.python.Python

class PyDiffieHellmanInstance(host: String){
    private val pythonInstance: Python = Python.getInstance()
    private var diffieHellman: PyObject = pythonInstance.getModule("MyCryptograpy.DiffieHellman")
    private val diffieHellmanObject: PyObject = diffieHellman.callAttr("DiffieHellmanConnection",host)

    fun getSharedKey(): PyObject{
        val key = diffieHellmanObject.callAttr("key_exchange")
        println(key.toString())
        return key
    }
}