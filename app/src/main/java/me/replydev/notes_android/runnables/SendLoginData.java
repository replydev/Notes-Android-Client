package me.replydev.notes_android.runnables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

public class SendLoginData implements Callable<String> {

    private final Socket s;
    private final String encryptedData;

    public SendLoginData(Socket s,String encryptedData){
        this.s = s;
        this.encryptedData = encryptedData;
    }

    @Override
    public String call() throws Exception {
        PrintWriter writer = new PrintWriter(s.getOutputStream(),true);
        writer.write(encryptedData);
        writer.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String s = reader.readLine();
        reader.close();
        return s;
    }
}
