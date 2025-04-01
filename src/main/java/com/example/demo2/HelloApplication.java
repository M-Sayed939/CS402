package com.example.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HelloApplication extends Application {
    private TextField keyField;
    private TextArea inputTextArea, outputTextArea;
    private ComboBox<String> actionComboBox, cipherComboBox;
    private CheckBox fileCheckBox;
    private final File defaultDirectory = new File("C:/Users/pc/Downloads/ID/demo2/Files");
    private boolean isSubstitutionKeyImported = false;
    private StringBuilder content = new StringBuilder();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cryptography");

        // Header
        Label header = new Label("Cryptography CS402");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Key Input
        HBox keyBox = new HBox(10, new Label("Key:"), keyField = new TextField());
        keyBox.setAlignment(Pos.CENTER);

        // Cipher Selection
        cipherComboBox = new ComboBox<>();
        cipherComboBox.getItems().addAll("Caesar", "Affine", "Substitution", "Playfair", "One Time Pad", "DES", "AES");
        cipherComboBox.getSelectionModel().selectFirst();
        HBox cipherBox = new HBox(10, new Label("Cipher:"), cipherComboBox);
        cipherBox.setAlignment(Pos.CENTER);

        // Action Selection
        actionComboBox = new ComboBox<>();
        actionComboBox.getItems().addAll("Encrypt", "Decrypt", "Attack");
        actionComboBox.getSelectionModel().selectFirst();
        HBox actionBox = new HBox(10, new Label("Action:"), actionComboBox);
        actionBox.setAlignment(Pos.CENTER);

        // Text Areas
        inputTextArea = new TextArea();
        inputTextArea.setPromptText("Enter text here...");
        inputTextArea.setWrapText(true);

        outputTextArea = new TextArea();
        outputTextArea.setPromptText("Result will be shown here...");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        // Buttons
        Button executeButton = new Button("Execute");
        Button importKeyButton = new Button("Import Key");
        Button importFileButton = new Button("Import File");
        Button exportButton = new Button("Export Result");
        Button generateKeyButton = new Button("Generate Key");
        fileCheckBox = new CheckBox("File Mode");

        HBox buttonBox = new HBox(10, executeButton, importKeyButton, importFileButton, exportButton, generateKeyButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Layout
        VBox layout = new VBox(15, header, keyBox, cipherBox, actionBox, fileCheckBox,
                new Label("Input Text:"), inputTextArea,
                new Label("Output Text:"), outputTextArea, buttonBox);
        layout.setPadding(new Insets(15));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #F0F0F0; -fx-border-radius: 10px; -fx-padding: 20px;");

        // Scene and Stage
        Scene scene = new Scene(layout, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
