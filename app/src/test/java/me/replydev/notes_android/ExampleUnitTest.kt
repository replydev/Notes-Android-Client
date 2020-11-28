package me.replydev.notes_android

import me.replydev.notes_android.utils.PasswordChecker
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun checkPwd() {
        println(PasswordChecker.checkPwd("W@127dfb"))
    }
}