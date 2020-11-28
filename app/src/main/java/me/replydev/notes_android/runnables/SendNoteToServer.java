package me.replydev.notes_android.runnables;

import com.google.gson.JsonObject;
import me.replydev.notes_android.Globals;
import me.replydev.notes_android.crypto.KeyGenerator;
import me.replydev.notes_android.crypto.PyXChaCha20Instance;
import me.replydev.notes_android.json.EncryptedBody;
import me.replydev.notes_android.json.Note;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SendNoteToServer implements Runnable{

    private final Note n;

    public SendNoteToServer(Note n){
        this.n = n;
    }

    @Override
    public void run() {
        String salt = this.n.getSalt();
        String json = Globals.INSTANCE.getGson().toJson(this.n);
        try {
            String key = KeyGenerator.generateKey(Globals.password,salt);
            PyXChaCha20Instance pyXChaCha20Instance = new PyXChaCha20Instance(key);
            String encryptedJson = pyXChaCha20Instance.encrypt(json);
            String jsonObjectThatWillBeStoredInServer = buildJson(this.n.getId(),Globals.INSTANCE.getUserId(),encryptedJson,salt);
            Globals.encryptedSocket.send(jsonObjectThatWillBeStoredInServer);
            String response = Globals.encryptedSocket.read();

            if(response.equalsIgnoreCase("ok")){
                System.out.println("Server has accepted new note");
            }
            else{
                System.out.println("Server hasn't accepted new note");
            }
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private String buildJson(int id,int author,String encryptedJson,String salt){
        EncryptedBody encryptedBody = Globals.INSTANCE.getGson().fromJson(encryptedJson,EncryptedBody.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",id);
        jsonObject.addProperty("author",author);
        jsonObject.addProperty("salt",salt);
        jsonObject.add("encrypted",Globals.INSTANCE.getGson().toJsonTree(encryptedBody));
        return jsonObject.toString();
    }
}
