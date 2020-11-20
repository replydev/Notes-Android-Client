package me.replydev.notes_android

import com.chaquo.python.PyObject
import com.chaquo.python.Python

class PyXChaCha20Instance(encryption_key: String) {

    private val pythonInstance: Python = Python.getInstance()
    private var cryptoModule: PyObject = pythonInstance.getModule("MyCryptograpy.XChaCha20Py")
    private val cryptoObject: PyObject = cryptoModule.callAttr("XChaCha20Crypto",encryption_key)

    fun encrypt(s: String): String {
        return cryptoObject.callAttr("encrypt",s).toString()
    }

    fun decrypt(s: String): String{
        return cryptoObject.callAttr("decrypt",s).toString()
    }
}