package com.example.demo2;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Cryptology {
    static final int ALPHABET_SIZE = 26;
    static String pText = "", cText = "", key = "";
    private static final String EnglishFreq = "ETAOINSHRDLCUMWFGYPBVKJXQZ";
    private Map<Character, Character> substitutionMap = new HashMap<>();
    private Map<Character, Character> reverseSubstitutionMap = new HashMap<>();

    public void setSubstitutionMap(Map<Character, Character> substitutionMap) {
        this.substitutionMap = substitutionMap;
        reverseSubstitutionMap.clear();
        for (Map.Entry<Character, Character> entry : substitutionMap.entrySet()) {
            reverseSubstitutionMap.put(entry.getValue(), entry.getKey());
            reverseSubstitutionMap.put(Character.toLowerCase(entry.getValue()), Character.toLowerCase(entry.getKey()));
        }
    }


    private boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    private int modInverse(int a) {
        a = a % Cryptology.ALPHABET_SIZE;
        for (int x = 1; x < Cryptology.ALPHABET_SIZE; x++) {
            if ((a * x) % Cryptology.ALPHABET_SIZE == 1) {
                return x;
            }
        }
        return 1;
    }

    private Map<Character, Character> generateKeyMap(String key) {
        Map<Character, Character> keyMap = new HashMap<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] keyAlphabet = key.toUpperCase().toCharArray();
        for (int i = 0; i < alphabet.length; i++) {
            keyMap.put(alphabet[i], keyAlphabet[i]);
        }
        return keyMap;
    }

    private Map<Character, Character> revgenerateKeyMap(String key) {
        Map<Character, Character> keyMap = new HashMap<>();
        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] keyAlphabet = key.toUpperCase().toCharArray();
        for (int i = 0; i < alphabet.length; i++) {
            keyMap.put(keyAlphabet[i], alphabet[i]);
        }
        return keyMap;
    }

    //QWERTYUIOPLKJHGFDSAZXCVBNM
    private char[][] generateKeyMatrix(String key) {
        char[][] keyMatrix = new char[5][5];
        String alphabet = "abcdefghiklmnopqrstuvwxyz";
        boolean[] used = new boolean[26];
        int index = 0;
        for (char ch : key.toCharArray()) {
            ch = Character.toLowerCase(ch);
            if (ch == 'j')
                ch = 'i';
            if (!used[ch - 'a']) {
                used[ch - 'a'] = true;
                keyMatrix[index / 5][index % 5] = ch;
                index++;
            }
        }
        for (char ch : alphabet.toCharArray()) {
            if (ch == 'j')
                ch = 'i';
            if (!used[ch - 'a']) {
                used[ch - 'a'] = true;
                keyMatrix[index / 5][index % 5] = ch;
                index++;
            }
        }
        return keyMatrix;
    }

    private String prepareText(String text) {
        text = text.toLowerCase().replace("j", "i").replaceAll("[^a-z]", "");
        StringBuilder prepared = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length()) ? text.charAt(i + 1) : 'x';
            prepared.append(a);
            if (a == b) {
                prepared.append('x');

            } else {
                prepared.append(b);
                i++;
            }
        }
        if (prepared.length() % 2 != 0) {
            prepared.append('x');
        }
        return prepared.toString();
    }

    private int[] findPosition(char[][] keyMatrix, char ch) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (keyMatrix[i][j] == ch) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }

    public String EncCaesar() {
        StringBuilder cText = new StringBuilder();
        for (int i = 0; i < pText.length(); i++) {
            char ch = pText.charAt(i);
            if (isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    char encryptedChar = (char) ('A' + ((ch - 'A') + Integer.parseInt(key)) % ALPHABET_SIZE);
                    cText.append(encryptedChar);
                } else {
                    char encryptedChar = (char) ('a' + ((ch - 'a') + Integer.parseInt(key)) % ALPHABET_SIZE);
                    cText.append(encryptedChar);
                }
            } else {
                cText.append(ch);
            }
        }
        return cText.toString();
    }

    public String DecCaesar() {
        StringBuilder pText = new StringBuilder();
        for (int i = 0; i < cText.length(); i++) {
            char ch = cText.charAt(i);
            if (isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    char decryptedChar = (char) ('A' + ((ch - 'A') - Integer.parseInt(key) + ALPHABET_SIZE) % ALPHABET_SIZE);
                    pText.append(decryptedChar);
                } else {
                    char decryptedChar = (char) ('a' + ((ch - 'a') - Integer.parseInt(key) + ALPHABET_SIZE) % ALPHABET_SIZE);
                    pText.append(decryptedChar);
                }
            } else {
                pText.append(ch);
            }
        }
        return pText.toString();
    }

    public String AttackCaesar() {
        StringBuilder result = new StringBuilder();
        for (int shift = 0; shift < ALPHABET_SIZE; shift++) {
            StringBuilder decryptedText = new StringBuilder();
            for (int i = 0; i < cText.length(); i++) {
                char ch = cText.charAt(i);
                if (isLetter(ch)) {
                    if (Character.isUpperCase(ch)) {
                        char decryptedChar = (char) ('A' + ((ch - 'A') - shift + ALPHABET_SIZE) % ALPHABET_SIZE);
                        decryptedText.append(decryptedChar);
                    } else {
                        char decryptedChar = (char) ('a' + ((ch - 'a') - shift + ALPHABET_SIZE) % ALPHABET_SIZE);
                        decryptedText.append(decryptedChar);
                    }
                } else {
                    decryptedText.append(ch);
                }
            }
            result.append("Shift ").append(shift).append(": ").append(decryptedText).append("\n");
        }
        return result.toString();
    }

    public String EncAffine() {
        String[] keys = key.split(" ");
        int a = Integer.parseInt(keys[0]);
        int b = Integer.parseInt(keys[1]);
        StringBuilder cText = new StringBuilder();
        for (int i = 0; i < pText.length(); i++) {
            char ch = pText.charAt(i);
            if (isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    char encryptedChar = (char) ('A' + ((a * (ch - 'A') + b) % ALPHABET_SIZE));
                    cText.append(encryptedChar);
                } else {
                    char encryptedChar = (char) ('a' + ((a * (ch - 'a') + b) % ALPHABET_SIZE));
                    cText.append(encryptedChar);
                }
            } else {
                cText.append(ch);
            }

        }
        return cText.toString();
    }

    public String DecAffine() {
        String[] keys = key.split(" ");
        int a = Integer.parseInt(keys[0]);
        int b = Integer.parseInt(keys[1]);
//        int aInverse = 0;
//        for (int i = 0; i < 26; i++) {
//            if (((a * i) % 26) == 1) {
//                aInverse = i;
//            }
//        }
        int aInverse = modInverse(a);
        StringBuilder pText = new StringBuilder();
        for (int i = 0; i < cText.length(); i++) {
            char ch = cText.charAt(i);
            if (isLetter(ch)) {
                if (Character.isUpperCase(ch)) {
                    char decryptedChar = (char) ('A' + (aInverse * (ch - 'A' - b + ALPHABET_SIZE) % ALPHABET_SIZE));
                    pText.append(decryptedChar);
                } else {
                    char decryptedChar = (char) ('a' + (aInverse * (ch - 'a' - b + ALPHABET_SIZE) % ALPHABET_SIZE));
                    pText.append(decryptedChar);
                }
            } else {
                pText.append(ch);
            }

        }
        return pText.toString();
    }

    public String encryptSubstitution(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (substitutionMap.containsKey(c)) {
                result.append(substitutionMap.get(c));
            } else if (substitutionMap.containsKey(Character.toLowerCase(c))) {
                result.append(Character.toUpperCase(substitutionMap.get(Character.toLowerCase(c))));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public String decryptSubstitution(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (reverseSubstitutionMap.containsKey(c)) {
                result.append(reverseSubstitutionMap.get(c));
            } else if (reverseSubstitutionMap.containsKey(Character.toLowerCase(c))) {
                result.append(Character.toUpperCase(reverseSubstitutionMap.get(Character.toLowerCase(c))));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public String AttackSubstitution() {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char ch : cText.toCharArray()) {
            if (Character.isLetter(ch)) {
                char upperCh = Character.toUpperCase(ch);
                freqMap.put(upperCh, freqMap.getOrDefault(upperCh, 0) + 1);
            }
        }
        Map<Character, Integer> sortedFreqMap = freqMap.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, HashMap::new));
        Map<Character, Character> decKeyMap = new HashMap<>();
        int index = 0;
        for (char ch : sortedFreqMap.keySet()) {
            decKeyMap.put(ch, EnglishFreq.charAt(index));
            index++;
        }
        StringBuilder pText = new StringBuilder();
        for (char ch : cText.toCharArray()) {
            if (Character.isLetter(ch)) {
                char upperCh = Character.toUpperCase(ch);
                char decryptedChar = decKeyMap.getOrDefault(upperCh, ch);
                pText.append(Character.isUpperCase(ch) ? decryptedChar : Character.toLowerCase(decryptedChar));
            } else {
                pText.append(ch);
            }
        }
        return pText.toString();
    }

    public String EncOne() {
        if (key.length() != pText.length())  {
            throw new IllegalArgumentException("Key length must be at least the same length as the plaintext");

        }
        StringBuilder cText = new StringBuilder();
        for (int i = 0; i < pText.length(); i++) {
            char ch = pText.charAt(i);
            char keyCh = key.charAt(i);
            if (isLetter(ch) && isLetter(keyCh)) {
                if (Character.isUpperCase(ch)) {
                    char encryptedChar = (char) ('A' + ((ch - 'A' + 26) + (keyCh - 'A'+26)) % ALPHABET_SIZE);
                    cText.append(encryptedChar);
                } else {
                    char encryptedChar = (char) ('a' + ((ch - 'a'+26) + (keyCh - 'a'+26)) % ALPHABET_SIZE);
                    cText.append(encryptedChar);
                }
            } else {
                cText.append(ch);
            }
        }
        return cText.toString();
    }


    public String DecOne() {
        StringBuilder pText = new StringBuilder();
        for (int i = 0; i < cText.length(); i++) {
            char ch = cText.charAt(i);
            char keyCh = key.charAt(i);
            if (isLetter(ch) && isLetter(keyCh)) {
                if (Character.isUpperCase(ch)) {
                    char decryptedChar = (char) ('A' + ((ch - 'A' ) - (keyCh - 'A') + ALPHABET_SIZE) % ALPHABET_SIZE);
                    pText.append(decryptedChar);
                } else {
                    char decryptedChar = (char) ('a' + ((ch - 'a') - (keyCh - 'a') + ALPHABET_SIZE) % ALPHABET_SIZE);
                    pText.append(decryptedChar);
                }
            } else {
                pText.append(ch);
            }

        }
        return pText.toString();
    }

    public String EncPlay() {
        char[][] keyMatrix = generateKeyMatrix(key);
        String prepared = prepareText(pText);
        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < prepared.length(); i += 2) {
            char a = prepared.charAt(i);
            char b = prepared.charAt(i + 1);
            int[] posA = findPosition(keyMatrix, a);
            int[] posB = findPosition(keyMatrix, b);
            if (posA[0] == posB[0]) {
                encryptedText.append(keyMatrix[posA[0]][(posA[1] + 1) % 5]);
                encryptedText.append(keyMatrix[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) {
                encryptedText.append(keyMatrix[(posA[0] + 1) % 5][posA[1]]);
                encryptedText.append(keyMatrix[(posB[0] + 1) % 5][posB[1]]);
            } else {
                encryptedText.append(keyMatrix[posA[0]][posB[1]]);
                encryptedText.append(keyMatrix[posB[0]][posA[1]]);
            }

        }
        return encryptedText.toString();
    }

    public String DecPlay() {
        char[][] keyMatrix = generateKeyMatrix(key);
        String prepared = prepareText(cText);
        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < prepared.length(); i += 2) {
            char a = prepared.charAt(i);
            char b = prepared.charAt(i + 1);
            int[] posA = findPosition(keyMatrix, a);
            int[] posB = findPosition(keyMatrix, b);
            if (posA[0] == posB[0]) {
                decryptedText.append(keyMatrix[posA[0]][(posA[1] - 1 + 5) % 5]);
                decryptedText.append(keyMatrix[posB[0]][(posB[1] - 1 + 5) % 5]);
            } else if (posA[1] == posB[1]) {
                decryptedText.append(keyMatrix[(posA[0] - 1 + 5) % 5][posA[1]]);
                decryptedText.append(keyMatrix[(posB[0] - 1 + 5) % 5][posB[1]]);
            } else {
                decryptedText.append(keyMatrix[posA[0]][posB[1]]);
                decryptedText.append(keyMatrix[posB[0]][posA[1]]);
            }

        }
        return decryptedText.toString();
    }


}
