package me.replydev.notes_android.net;

import me.replydev.notes_android.crypto.PyXChaCha20Instance;

import java.io.*;
import java.net.Socket;

public class SimpleEncryptedSocket implements Closeable {

    public static final int BUFFER_SIZE = 1024;
    private final Socket s;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private PyXChaCha20Instance pyXChaCha20Instance;

    public SimpleEncryptedSocket(String host, int port) throws IOException {
        s = new Socket(host,port);
        writer = new PrintWriter(s.getOutputStream(),true);
        reader = new BufferedReader (new InputStreamReader(s.getInputStream()));
    }

    public String read() throws IOException {
        int charsRead;
        char[] buffer = new char[BUFFER_SIZE];
        charsRead = reader.read(buffer);
        if(charsRead != -1){
            String encryptedString = String.valueOf(buffer).substring(0, charsRead);
            return pyXChaCha20Instance.decrypt(encryptedString);
        }
        else return null;
    }

    public void send(String s){
        String encryptedJson = pyXChaCha20Instance.encrypt(s);
        writer.println(encryptedJson);
    }

    public void setEncryptionInstance(PyXChaCha20Instance encryptionInstance){
        if(pyXChaCha20Instance == null){
            pyXChaCha20Instance = encryptionInstance;
        }
        else{
            System.err.println("PyXChaCha20 instance already instantiated!");
        }
    }

    public boolean isClosed(){
        return s.isClosed();
    }

    @Override
    public void close() throws IOException {
        writer.close();
        reader.close();
        s.close();
    }
}
