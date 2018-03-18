package com.rizaldi.cipher;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DesCipher {
    private Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
    private KeyGenerator generator = KeyGenerator.getInstance("DES");

    public DesCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    public String getKey() {
        SecretKey key = generator.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public byte[] encrypt(String key, byte[] text) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
        return cipher.doFinal(text);
    }

    public byte[] decrypt(String key, byte[] text) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
        return cipher.doFinal(text);
    }

    private SecretKey getSecretKey(String key) {
        byte[] bytes = Base64.getDecoder().decode(key);
        return new SecretKeySpec(bytes, 0, bytes.length, "DES");
    }
}
