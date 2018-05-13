package com.rizaldi.cipher;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class RsaKey {
    private static final int key_size = 4096;
    private static final KeyFactory factory = initFactory();
    private static final KeyPairGenerator generator = initGenerator();

    private KeyPair keyPair = generator.genKeyPair();
    private PrivateKey privateKey = keyPair.getPrivate();
    private PublicKey publicKey = keyPair.getPublic();

    private static KeyFactory initFactory() {
        try {
            return KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyPairGenerator initGenerator() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(key_size);
            return generator;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    PrivateKey getPrivateKey() {
        return privateKey;
    }

    PublicKey getPublicKey() {
        return publicKey;
    }

    void generateKey() {
        keyPair = generator.genKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    void savePrivateKey(File file) throws IOException {
        byte[] bytes = new PKCS8EncodedKeySpec(privateKey.getEncoded()).getEncoded();
        FileUtils.writeByteArrayToFile(file, bytes);
    }

    void loadPrivateKey(File file) throws IOException, InvalidKeySpecException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    void savePublicKey(File file) throws IOException {
        byte[] bytes = new X509EncodedKeySpec(publicKey.getEncoded()).getEncoded();
        FileUtils.writeByteArrayToFile(file, bytes);
    }

    void loadPublicKey(File file) throws IOException, InvalidKeySpecException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        publicKey = factory.generatePublic(new X509EncodedKeySpec(bytes));
    }
}
