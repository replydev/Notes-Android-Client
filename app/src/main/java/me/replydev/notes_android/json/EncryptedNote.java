package me.replydev.notes_android.json;

import com.google.gson.annotations.SerializedName;

public class EncryptedNote {

    private int id;
    private String salt;
    @SerializedName("encrypted")
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

