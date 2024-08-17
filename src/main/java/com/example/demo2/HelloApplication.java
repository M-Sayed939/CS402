package com.example.demo2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HelloApplication extends Application {
    private TextField keyField;
    private TextArea inputTextArea;
    private TextArea outputTextArea;
    private ComboBox<String> actionComboBox;
    private ComboBox<String> cipherComboBox;
    private Cryptology cryptology;
    private DES des=new
            DES();
    private CheckBox fileCheckBox;
    private final File defaultDirectory = new File("C:\\Users\\pc\\Downloads\\ID\\demo2\\Files");
    private boolean isSubstitutionKeyImported = false;

    @Override
    public void start(Stage primaryStage) {
        cryptology = new Cryptology();
        Label keyLabel = new Label("Key:");
        keyField = new TextField();
        keyField.setPrefWidth(50);

        Label cipherLabel = new Label("Cipher:");
        cipherComboBox = new ComboBox<>();
        cipherComboBox.getItems().addAll("Caesar", "Affine", "Substitution","Playfair","One Time Pad","DES");
        cipherComboBox.getSelectionModel().selectFirst();


        Label actionLabel = new Label("Action:");
        actionComboBox = new ComboBox<>();
        actionComboBox.getItems().addAll("Encrypt", "Decrypt","Attack");
        actionComboBox.getSelectionModel().selectFirst();

        Button importKeyButton = new Button("Import Key");
        importKeyButton.setOnAction(e -> importKey());

        Button executeButton = new Button("Execute");
        executeButton.setOnAction(e -> cipherAlgorithm());

        Button exportResultButton = new Button("Export Result");
        exportResultButton.setOnAction(e -> exportResult());

        inputTextArea = new TextArea();
        inputTextArea.setPromptText("Enter text here...");
        inputTextArea.setWrapText(true);

        fileCheckBox = new CheckBox("File Mode");
        fileCheckBox.setOnAction(e -> handleFileMode());
        Button importFileButton = new Button("Import File");
        importFileButton.setOnAction(e -> importFile());

        outputTextArea = new TextArea();
        outputTextArea.setPromptText("Result will be shown here...");
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        Button generateKeyButton = new Button("Generate Key");
        generateKeyButton.setOnAction(e -> generateOneTimePadKey());
        Button printRoundBtn = new Button("Print Rounds");
        printRoundBtn.setDisable(true);
        printRoundBtn.setOnAction(e -> printRoundsToFile());
        cipherComboBox.setOnAction(e -> {
            String selectedcipher = cipherComboBox.getValue();
            printRoundBtn.setDisable(!"DES".equals(selectedcipher));
        });

        HBox keyBox = new HBox(10, keyLabel, keyField, importKeyButton, generateKeyButton);
        keyBox.setAlignment(Pos.CENTER_LEFT);
        HBox cipherBox = new HBox(10, cipherLabel, cipherComboBox);
        cipherBox.setAlignment(Pos.CENTER_LEFT);

        HBox actionBox = new HBox(10, actionLabel, actionComboBox, executeButton, exportResultButton,printRoundBtn);
        actionBox.setAlignment(Pos.CENTER_LEFT);


        VBox layout = new VBox(10, keyBox,cipherBox, actionBox, new Label("Input Text:"), inputTextArea,fileCheckBox,importFileButton, new Label("Output Text:"), outputTextArea);
        layout.setPadding(new Insets(10));
        layout.setStyle("-fx-background-color: #F0F0F0;");
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void generateOneTimePadKey() {
        String sCipher = cipherComboBox.getValue();
        if (!"One Time Pad".equals(sCipher)) {
            showAlert("Key generation is only supported for One Time Pad cipher.");
            return;
        }
        String inputText = inputTextArea.getText();
        System.out.println(inputText.length());
        if (inputText.isEmpty()) {
            showAlert("Please enter text to generate a key.");
            return;
        }

        StringBuilder keyBuilder = new StringBuilder();
        int seed = 13;
        int prev = seed;
        for (int i = 0; i < inputText.length(); i++) {
            int next = (5 * prev + 13) % 26;
            System.out.println(next);
            char randomChar = (char) ('A' + next);
            keyBuilder.append(randomChar);
            prev = next;
        }
        keyField.setText(keyBuilder.toString());
    }

    private void handleFileMode() {
        boolean isFileMode = fileCheckBox.isSelected();
        inputTextArea.setEditable(!isFileMode);
        inputTextArea.setText("");
    }
    private void importFile() {
        if (!fileCheckBox.isSelected()) {
            showAlert("File mode is not enabled.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import File");
        fileChooser.setInitialDirectory(defaultDirectory);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String text = new String(Files.readAllBytes(Paths.get(file.getPath())));
                inputTextArea.setText(text);
            } catch (IOException e) {
                showAlert("Failed to import file: " + e.getMessage());
            }
        }
    }

    private void importKey() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Key");
        fileChooser.setInitialDirectory(defaultDirectory);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                if ("Substitution".equals(cipherComboBox.getValue())) {
                    readSubstitutionKey(file);
                    isSubstitutionKeyImported = true;
                } else {
                    String key = new String(Files.readAllBytes(Paths.get(file.getPath())));
                    keyField.setText(key.trim());
                }
            } catch (IOException e) {
                showAlert("Failed to import key: " + e.getMessage());
            }
        }
    }

    private void readSubstitutionKey(File file) throws IOException {
        Map<Character, Character> substitutionMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("->");
            if (parts.length == 2) {
                char key = parts[0].trim().charAt(0);
                char value = parts[1].trim().charAt(0);
                substitutionMap.put(key, value);
                substitutionMap.put(Character.toUpperCase(key), Character.toUpperCase(value));
                substitutionMap.put(Character.toLowerCase(key), Character.toLowerCase(value));
            }
        }
        reader.close();
        cryptology.setSubstitutionMap(substitutionMap);
    }
    private void printRoundsToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Rounds");
        fileChooser.setInitialDirectory(defaultDirectory);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                for(String round: des.getRounds()){
                writer.write(round);
                writer.newLine();
                }
                showAlert("Rounds successfully printed to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Failed to print rounds: " + e.getMessage());
            }
        }
    }

    private void exportResult() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Result");
        fileChooser.setInitialDirectory(defaultDirectory);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(outputTextArea.getText());
                showAlert("Result successfully exported to " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Failed to export result: " + e.getMessage());
            }
//            try {
//                Files.write(Paths.get(file.getPath()), outputTextArea.getText().getBytes());
//            } catch (IOException e) {
//                showAlert("Failed to export result: " + e.getMessage());
//            }
        }
    }
    private void cipherAlgorithm(){
        String keyText = keyField.getText().trim();
        String inputText = inputTextArea.getText();
        String action = actionComboBox.getValue();
        String cipher = cipherComboBox.getValue();
        String result = "";
        Cryptology.key = keyText;
        boolean isValid = true;
        switch (cipher) {
            case "Affine" -> {
                if (!keyText.matches("\\d+ \\d+")) {
                    isValid = false;
                    showAlert("Invalid key format for Affine cipher. Please enter two integers separated by a space.");
                }
            }
//            case "One Time Pad" -> {
//                if (!keyText.matches("[A-Z]+") || keyText.length() != inputText.length()) {
//                    isValid = false;
//                    showAlert("Invalid key format for One Time Pad cipher. Please enter a key with the same length as the input text.");
//                }
//            }
            case "Playfair"->{
                if (keyText.matches("\\d+")){
                    isValid = false;
                    showAlert("Invalid key format for Playfair Cipher. Please enter an alphabetic.");
                }
            }
            case "Substitution" -> {
                if (keyText.matches("\\d+")) {
                    isValid = false;
                    showAlert("Invalid key format for Substitution Cipher. Please enter an alphabetic.");
                }
            }
            case "DES" -> {
                if (keyText.length()!=8) {
                    isValid = false;
                    showAlert("Invalid key format for DES Cipher. Please enter a 8-bit key.");
                }
            }
//            default -> {
//                if (!keyText.matches("\\d+")) {
//                    isValid = false;
//                    showAlert("Invalid key format. Please enter an integer.");
//                }
//            }
        }
        if (!isValid) {
            return;
        }
        switch (cipher) {
            case "Caesar" -> {
                if ("Encrypt".equals(action)) {
                    Cryptology.pText = inputText;
                    result = cryptology.EncCaesar();
                } else if ("Decrypt".equals(action)) {
                    Cryptology.cText = inputText;
                    result = cryptology.DecCaesar();
                } else if ("Attack".equals(action)) {
                    Cryptology.cText = inputText;
                    result = cryptology.AttackCaesar();

                }
            }
            case "Affine" -> {
                if ("Encrypt".equals(action)) {
                    Cryptology.pText = inputText;
                    result = cryptology.EncAffine();
                } else if ("Decrypt".equals(action)) {
                    Cryptology.cText = inputText;
                    result = cryptology.DecAffine();
                } else if ("Attack".equals(action)) {
                    showAlert("Attack functionality is not implemented for Affine cipher.");
                    return;
                }
            }
            case "Substitution" -> {
                if (!isSubstitutionKeyImported) {
                    showAlert("Please import a valid substitution key.");
                    return;
                }
                if ("Encrypt".equals(action)) {
                    result = cryptology.encryptSubstitution(inputText);
                } else if ("Decrypt".equals(action)) {
                    result = cryptology.decryptSubstitution(inputText);
                } else if ("Attack".equals(action)) {
                    showAlert("Attack functionality is not implemented for Substitution cipher.");
                    return;
                }
            }
            case "One Time Pad" -> {
                if ("Encrypt".equals(action)) {
                    Cryptology.pText = inputText;
                    result = cryptology.EncOne();
                } else if ("Decrypt".equals(action)) {
                    Cryptology.cText = inputText;
                    result = cryptology.DecOne();
                } else if ("Attack".equals(action)) {
                    showAlert("Attack functionality is not implemented for One Time Pad cipher.");
                    return;
                }
            }
            case "Playfair" -> {
                if ("Encrypt".equals(action)) {
                    Cryptology.pText = inputText;
                    result = cryptology.EncPlay();
                } else if ("Decrypt".equals(action)) {
                    Cryptology.cText = inputText;
                    result = cryptology.DecPlay();
                } else if ("Attack".equals(action)) {
                    showAlert("Attack functionality is not implemented for Playfair cipher.");
                    return;
                }
            }
            case "DES" -> {
                des.setKey(keyText);
                des.setInputText(inputText);
                if ("Encrypt".equals(action)) {
                    des.encDES();
                } else if ("Decrypt".equals(action)) {
                    des.decDES();
                } else {
                    showAlert("Attack functionality is not implemented for DES cipher.");
                    return;
                }
                result = des.getOutputText();
            }
        }
        outputTextArea.setText(result);
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}