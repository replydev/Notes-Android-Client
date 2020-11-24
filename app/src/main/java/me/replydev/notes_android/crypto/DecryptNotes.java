package me.replydev.notes_android.crypto;

import me.replydev.notes_android.Globals;
import me.replydev.notes_android.json.EncryptedNote;
import me.replydev.notes_android.json.Note;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class DecryptNotes implements Callable<ArrayList<Note>> {
    private final ArrayList<EncryptedNote> encryptedNotes;
    private final String password;

    public DecryptNotes(ArrayList<EncryptedNote> encryptedNotes, String password){
        this.encryptedNotes = encryptedNotes;
        this.password = password;
    }

    @Override
    public ArrayList<Note> call() throws Exception {
        ArrayList<Note> notes = new ArrayList<>();

        for(EncryptedNote encryptedNote : encryptedNotes){
            notes.add(decrypt(encryptedNote));
        }

        return notes;
    }

    public Note decrypt(EncryptedNote encryptedNote) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String salt = encryptedNote.getSalt();
        String encryptedJson = Globals.Companion.getGson().toJson(encryptedNote.getEncryptedJson());
        PyXChaCha20Instance pyXChaCha20Instance = new PyXChaCha20Instance(KeyGenerator.generateKey(password,salt));
        String decryptedJson = pyXChaCha20Instance.decrypt(encryptedJson);
        return Globals.Companion.getGson().fromJson(decryptedJson,Note.class);
    }
}
