package com.rizaldi.cipher;

import com.google.common.primitives.Bytes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Controller {
    @FXML
    private TextArea originalView;
    @FXML
    private TextArea resultView;
    @FXML
    private TextField publicKeyView;
    @FXML
    private TextField privateKeyView;

    private static final int encrypt_block = 501;
    private static final int decrypt_block = 512;
    private static final int key_size = 4096;

    private final FileChooser chooser = new FileChooser();
    private final Cipher cipher = Cipher.getInstance("RSA");
    private final KeyFactory factory = KeyFactory.getInstance("RSA");
    private final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private byte[] originalBytes;
    private byte[] resultBytes;

    public Controller() throws NoSuchPaddingException, NoSuchAlgorithmException {
    }

    @FXML
    private void onOpenFile(ActionEvent event) throws IOException {
        originalBytes = loadFileWithDialog(event);
        resultView.clear();
        originalView.setText(new String(originalBytes));
    }

    @FXML
    private void onSaveFile(ActionEvent event) throws IOException {
        saveFileWithDialog(event, resultBytes);
    }

    @FXML
    private void onEncrypt() {
        resultView.clear();
        if (privateKey == null) onGenerate();
        try {
            resultBytes = splitEncrypt();
            resultView.setText(new String(resultBytes));
        } catch (Exception e) {
            resultView.setText(e.toString());
        }
    }

    @FXML
    private void onDecrypt() {
        resultView.clear();
        if (publicKey == null) onGenerate();
        try {
            resultBytes = splitDecrypt();
            resultView.setText(new String(resultBytes));
        } catch (Exception e) {
            resultView.setText(e.toString());
        }
    }

    @FXML
    private void onGenerate() {
        generator.initialize(key_size);
        KeyPair pair = generator.genKeyPair();
        setPublicKey(pair.getPublic());
        setPrivateKey(pair.getPrivate());
    }

    @FXML
    private void onSavePrivateKey(ActionEvent event) throws IOException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        saveFileWithDialog(event, spec.getEncoded());
    }

    @FXML
    private void onLoadPrivateKey(ActionEvent event) throws InvalidKeySpecException, IOException {
        byte[] bytes = loadFileWithDialog(event);
        setPrivateKey(factory.generatePrivate(new PKCS8EncodedKeySpec(bytes)));
    }

    private void setPrivateKey(PrivateKey key) {
        privateKey = key;
        privateKeyView.setText(new String(key.getEncoded()));
    }

    @FXML
    private void onSavePublicKey(ActionEvent event) throws IOException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
        saveFileWithDialog(event, spec.getEncoded());
    }

    @FXML
    private void onLoadPublicKey(ActionEvent event) throws InvalidKeySpecException, IOException {
        byte[] bytes = loadFileWithDialog(event);
        setPublicKey(factory.generatePublic(new X509EncodedKeySpec(bytes)));
    }

    private void setPublicKey(PublicKey key) {
        publicKey = key;
        publicKeyView.setText(new String(key.getEncoded()));
    }

    private byte[] splitEncrypt() throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        List<Byte> result = new LinkedList<>();
        for (int i = 0; i < originalBytes.length; i += encrypt_block) {
            byte[] encryptedBytes = encrypt(Arrays.copyOfRange(originalBytes, i, i + encrypt_block));
            for (byte encryptedByte : encryptedBytes) result.add(encryptedByte);
        }
        return Bytes.toArray(result);
    }

    private byte[] encrypt(byte[] original) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(original);
    }

    private byte[] splitDecrypt() throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        List<Byte> result = new LinkedList<>();
        for (int i = 0; i < originalBytes.length; i += decrypt_block) {
            byte[] decryptedBytes = decrypt(Arrays.copyOfRange(originalBytes, i, i + decrypt_block));
            for (byte decryptedByte : decryptedBytes) result.add(decryptedByte);
        }
        return Bytes.toArray(result);
    }

    private byte[] decrypt(byte[] original) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(original);
    }

    private void saveFileWithDialog(ActionEvent event, byte[] bytes) throws IOException {
        File file = chooser.showSaveDialog(getWindowFromEvent(event));
        if (file != null) FileUtils.writeByteArrayToFile(file, bytes);
    }

    private byte[] loadFileWithDialog(ActionEvent event) throws IOException {
        File file = chooser.showOpenDialog(getWindowFromEvent(event));
        if (file != null) return FileUtils.readFileToByteArray(file);
        throw new IOException("no file");
    }

    private Window getWindowFromEvent(ActionEvent event) {
        return ((Node) event.getTarget()).getScene().getWindow();
    }
}
