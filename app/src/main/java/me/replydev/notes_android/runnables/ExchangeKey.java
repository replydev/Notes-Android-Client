package me.replydev.notes_android.runnables;

import me.replydev.notes_android.crypto.PyDiffieHellmanInstance;
import me.replydev.notes_android.crypto.PyXChaCha20Instance;

import java.util.concurrent.Callable;

public class ExchangeKey implements Callable<PyXChaCha20Instance> {
    private final String ipAddress;

    public ExchangeKey(String ipAddress){
        this.ipAddress = ipAddress;
    }

    @Override
    public PyXChaCha20Instance call() {
        PyDiffieHellmanInstance pyDiffieHellmanInstance = new PyDiffieHellmanInstance(ipAddress);
        String sharedKeyForServerCommunications = pyDiffieHellmanInstance.getSharedKey().toString();
        return new PyXChaCha20Instance(sharedKeyForServerCommunications);
    }
}
