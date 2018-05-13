package com.rizaldi.cipher;

import com.google.common.primitives.Bytes;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class RsaCipher {
    private static final int encrypt_block = 501;
    private static final int decrypt_block = 512;

    private final Cipher cipher = initCipher();

    private static Cipher initCipher() {
        try {
            return Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    byte[] encrypt(byte[] original, PublicKey key) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return executeCipher(original, encrypt_block);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    byte[] decrypt(byte[] original, PrivateKey key) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return executeCipher(original, decrypt_block);
        } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] executeCipher(byte[] original, int blockSize) throws BadPaddingException, IllegalBlockSizeException {
        List<Byte> result = new LinkedList<>();
        for (int i = 0; i < original.length; i += blockSize) {
            byte[] block = splitBlock(original, i, blockSize);
            byte[] encryptedBlock = cipher.doFinal(block);
            result.addAll(Bytes.asList(encryptedBlock));
        }
        return Bytes.toArray(result);
    }

    private byte[] splitBlock(byte[] original, int startIdx, int blockSize) {
        return Arrays.copyOfRange(original, startIdx, startIdx + blockSize);
    }
}
