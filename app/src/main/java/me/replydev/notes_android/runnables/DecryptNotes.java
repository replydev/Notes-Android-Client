package me.replydev.notes_android.runnables;

import me.replydev.notes_android.Globals;
import me.replydev.notes_android.crypto.KeyGenerator;
import me.replydev.notes_android.crypto.PyXChaCha20Instance;
import me.replydev.notes_android.json.EncryptedNote;
import me.replydev.notes_android.json.Note;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class DecryptNotes implements Runnable{
    private final ArrayList<EncryptedNote> encryptedNotes;
    private final String password;
    private final ArrayList<Note> notes;

    public DecryptNotes(ArrayList<EncryptedNote> encryptedNotes, String password, ArrayList<Note> notes){
        this.encryptedNotes = encryptedNotes;
        this.password = password;
        this.notes = notes;
    }

    @Override
    public void run(){
        for(EncryptedNote encryptedNote : encryptedNotes){
            try {
                notes.add(decrypt(encryptedNote));
            } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public Note decrypt(EncryptedNote encryptedNote) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String salt = encryptedNote.getSalt();
        String encryptedJson = Globals.INSTANCE.getGson().toJson(encryptedNote.getEncryptedJson());
        PyXChaCha20Instance pyXChaCha20Instance = new PyXChaCha20Instance(KeyGenerator.generateKey(password,salt));
        String decryptedJson = pyXChaCha20Instance.decrypt(encryptedJson);
        return Globals.INSTANCE.getGson().fromJson(decryptedJson,Note.class);
    }
}
