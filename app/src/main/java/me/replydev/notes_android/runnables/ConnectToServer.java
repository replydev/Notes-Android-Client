package me.replydev.notes_android.runnables;

import me.replydev.notes_android.SimpleSocket;

import java.util.concurrent.Callable;

public class ConnectToServer implements Callable<SimpleSocket> {

    private final String ipAddress;

    public ConnectToServer(String ipAddress){
        this.ipAddress = ipAddress;
    }

    @Override
    public SimpleSocket call() throws Exception {
        return new SimpleSocket(ipAddress, 50000);
    }
}
