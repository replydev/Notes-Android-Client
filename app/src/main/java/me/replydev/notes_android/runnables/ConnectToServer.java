package me.replydev.notes_android.runnables;

import me.replydev.notes_android.net.SimpleEncryptedSocket;

import java.util.concurrent.Callable;

public class ConnectToServer implements Callable<SimpleEncryptedSocket> {

    private final String ipAddress;

    public ConnectToServer(String ipAddress){
        this.ipAddress = ipAddress;
    }

    @Override
    public SimpleEncryptedSocket call() throws Exception {
        return new SimpleEncryptedSocket(ipAddress, 50000);
    }
}
