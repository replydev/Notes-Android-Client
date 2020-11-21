package me.replydev.notes_android;

public class EncryptedNote {

    private String salt;
    private EncryptedBody encryptedJson;





}

class EncryptedBody{
    private String nonce;
    private String salt;
    private String header;
    private String cipher_text;
    private String tag;

    public String getNonce() {
        return nonce;
    }

    public String getSalt() {
        return salt;
    }

    public String getHeader() {
        return header;
    }

    public String getCipher_text() {
        return cipher_text;
    }

    public String getTag() {
        return tag;
    }
}
