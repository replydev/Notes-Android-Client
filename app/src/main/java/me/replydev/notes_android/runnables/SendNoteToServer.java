package me.replydev.notes_android.runnables;

import com.google.gson.Gson;
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
        String json = Globals.Companion.getGson().toJson(this.n);
        try {
            String key = KeyGenerator.generateKey(Globals.password,salt);
            PyXChaCha20Instance pyXChaCha20Instance = new PyXChaCha20Instance(key);
            String encryptedJson = pyXChaCha20Instance.encrypt(json);
            String jsonObjectThatWillBeStoredInServer = buildJson(this.n.getId(),Globals.Companion.getUserId(),encryptedJson,salt);
            String encryptedJsonObjectThatWillBeStoredInServer = Globals.pyCryptoForServerCommunications.encrypt(jsonObjectThatWillBeStoredInServer);
            Globals.socket.send(encryptedJsonObjectThatWillBeStoredInServer);

            String response = Globals.socket.read();

            if(response.equalsIgnoreCase("yes")){
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
        EncryptedBody encryptedBody = Globals.Companion.getGson().fromJson(encryptedJson,EncryptedBody.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id",id);
        jsonObject.addProperty("author",author);
        jsonObject.addProperty("salt",salt);
        jsonObject.add("encrypted",Globals.Companion.getGson().toJsonTree(encryptedBody));
            /*
            {
	            "salt": $generated_salt,
	            "encrypted": {
                    'nonce': b64encode(nonce).decode('utf-8'),
                    'salt': b64encode(salt).decode('utf-8'),
                    'header': b64encode(header).decode('utf-8'),
                    'cipher_text': b64encode(cipher_text).decode('utf-8'),
                    'tag': b64encode(tag).decode('utf-8')
	            }
            }
             */
        return jsonObject.toString();
    }
}
