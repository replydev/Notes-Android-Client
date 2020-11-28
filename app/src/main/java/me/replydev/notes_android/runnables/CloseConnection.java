package me.replydev.notes_android.runnables;

import me.replydev.notes_android.Globals;

import java.io.IOException;

public class CloseConnection implements Runnable{
    @Override
    public void run() {
        try {
            Globals.INSTANCE.getEncryptedSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
