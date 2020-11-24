package me.replydev.notes_android.json;

public class EncryptedNote {

    private int id;
    private String salt;
    private EncryptedBody encryptedJson;

    public int getId() {
        return id;
    }

    public String getSalt() {
        return salt;
    }

    public EncryptedBody getEncryptedJson() {
        return encryptedJson;
    }
}

