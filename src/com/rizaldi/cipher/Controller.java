package com.rizaldi.cipher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.commons.io.FileUtils;

import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Controller {
    private final FileChooser chooser = new FileChooser();
    private final DesCipher cipher = new DesCipher();
    @FXML private TextField pathPreview;
    @FXML private TextArea filePreview;
    @FXML private TextArea cipherPreview;
    @FXML private TextField keyInput;
    private byte[] text;
    private byte[] cipherText;

    public Controller() throws NoSuchAlgorithmException, NoSuchPaddingException {}

    @FXML private void onOpenFile(ActionEvent event) throws IOException {
        File file = chooser.showOpenDialog(getWindowFromEvent(event));
        if (file == null) return;
        text = FileUtils.readFileToByteArray(file);
        pathPreview.setText(file.getAbsolutePath());
        cipherPreview.clear();
        filePreview.setText(new String(text));
    }

    @FXML private void onSaveFile(ActionEvent event) throws IOException {
        File file = chooser.showSaveDialog(getWindowFromEvent(event));
        if (file == null) return;
        FileUtils.writeByteArrayToFile(file, cipherText);
    }

    @FXML private void onGenerate(ActionEvent event) {
        keyInput.setText(cipher.getKey());
    }

    @FXML private void onEncrypt(ActionEvent event) {
        cipherPreview.clear();
        try {
            cipherText = cipher.encrypt(getKey(), text);
            cipherPreview.setText(new String(cipherText));
        } catch (Exception e) {
            cipherPreview.setText("Wrong Encrypt Key.\n" + e.toString());
        }
    }

    @FXML private void onDecrypt(ActionEvent event) {
        cipherPreview.clear();
        try {
            cipherText = cipher.decrypt(getKey(), text);
            cipherPreview.setText(new String(cipherText));
        } catch (Exception e) {
            cipherPreview.setText("Wrong Decrypt Key.\n" + e.toString());
        }
    }

    private String getKey() {
        return keyInput.getText();
    }

    private Window getWindowFromEvent(ActionEvent event) {
        return ((Node) event.getTarget()).getScene().getWindow();
    }
}
