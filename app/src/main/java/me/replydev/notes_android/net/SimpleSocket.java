package me.replydev.notes_android.net;

import java.io.*;
import java.net.Socket;

public class SimpleSocket implements Closeable {

    public static final int BUFFER_SIZE = 1024;
    private final Socket s;
    private final PrintWriter writer;
    private final BufferedReader reader;

    public SimpleSocket(String host,int port) throws IOException {
        s = new Socket(host,port);
        writer = new PrintWriter(s.getOutputStream(),true);
        reader = new BufferedReader (new InputStreamReader(s.getInputStream()));
    }

    public String read() throws IOException {
        int charsRead;
        char[] buffer = new char[BUFFER_SIZE];
        charsRead = reader.read(buffer);
        if(charsRead != -1){
            return String.valueOf(buffer).substring(0, charsRead);
        }
        else return null;
    }

    public void send(String s){
        writer.println(s);
    }

    @Override
    public void close() throws IOException {
        writer.close();
        reader.close();
        s.close();
    }
}
