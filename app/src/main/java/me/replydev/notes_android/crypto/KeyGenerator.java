package me.replydev.notes_android.crypto;

import android.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class KeyGenerator {
    public static String generateKey(String password,String salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 100000, 256);
        SecretKey tmp = factory.generateSecret(spec);
        byte[] keyBytes = tmp.getEncoded();
        return Base64.encodeToString(keyBytes,Base64.DEFAULT);
    }
}
