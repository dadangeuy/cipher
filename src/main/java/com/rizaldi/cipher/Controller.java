package com.rizaldi.cipher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.net.URL;
import java.security.spec.InvalidKeySpecException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextArea originalView;
    @FXML
    private TextArea resultView;
    @FXML
    private TextField publicKeyView;
    @FXML
    private TextField privateKeyView;

    private static final FileChooser chooser = new FileChooser();
    private static final RsaCipher cipher = new RsaCipher();
    private static final RsaKey key = new RsaKey();

    private byte[] originalBytes;
    private byte[] resultBytes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refreshKeyView();
    }

    @FXML
    private void onOpenFile(ActionEvent event) throws IOException {
        resultBytes = new byte[0];
        originalBytes = FileUtils.readFileToByteArray(chooser.showOpenDialog(getWindowFromEvent(event)));
        refreshPreview();
    }

    @FXML
    private void onSaveFile(ActionEvent event) throws IOException {
        FileUtils.writeByteArrayToFile(chooser.showSaveDialog(getWindowFromEvent(event)), resultBytes);
    }

    @FXML
    private void onEncrypt() {
        try {
            resultBytes = cipher.encrypt(originalBytes, key.getPublicKey());
        } catch (Exception e) {
            resultBytes = e.toString().getBytes();
        }
        refreshPreview();
    }

    @FXML
    private void onDecrypt() {
        try {
            resultBytes = cipher.decrypt(originalBytes, key.getPrivateKey());
        } catch (Exception e) {
            resultBytes = e.toString().getBytes();
        }
        refreshPreview();
    }

    @FXML
    private void onGenerate() {
        key.generateKey();
        refreshKeyView();
    }

    @FXML
    private void onSavePrivateKey(ActionEvent event) throws IOException {
        key.savePrivateKey(chooser.showSaveDialog(getWindowFromEvent(event)));
    }

    @FXML
    private void onLoadPrivateKey(ActionEvent event) throws InvalidKeySpecException, IOException {
        key.loadPrivateKey(chooser.showOpenDialog(getWindowFromEvent(event)));
        refreshKeyView();
    }

    @FXML
    private void onSavePublicKey(ActionEvent event) throws IOException {
        key.savePublicKey(chooser.showSaveDialog(getWindowFromEvent(event)));
    }

    @FXML
    private void onLoadPublicKey(ActionEvent event) throws InvalidKeySpecException, IOException {
        key.loadPublicKey(chooser.showSaveDialog(getWindowFromEvent(event)));
        refreshKeyView();
    }

    private void refreshPreview() {
        originalView.setText(new String(originalBytes));
        resultView.setText(new String(resultBytes));
    }

    private void refreshKeyView() {
        privateKeyView.setText(new String(key.getPrivateKey().getEncoded()));
        publicKeyView.setText(new String(key.getPublicKey().getEncoded()));
    }

    private Window getWindowFromEvent(ActionEvent event) {
        return ((Node) event.getTarget()).getScene().getWindow();
    }
}
