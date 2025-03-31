package com.example.demo2;


import java.util.ArrayList;
import java.util.List;


public class AES {

    private String[][] state;
    private String[][] roundKeys;
    private List<String> rounds;
    private int numberOfRounds;

    public static final byte[][] MIX_COLUMNS_MATRIX = {
            {(byte) 0x02, (byte) 0x03, (byte) 0x01, (byte) 0x01},
            {(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x01},
            {(byte) 0x01, (byte) 0x01, (byte) 0x02, (byte) 0x03},
            {(byte) 0x03, (byte) 0x01, (byte) 0x01, (byte) 0x02}
    };

    public static final byte[][] INV_MIX_COLUMNS_MATRIX = {
            {(byte) 0x0e, (byte) 0x0b, (byte) 0x0d, (byte) 0x09},
            {(byte) 0x09, (byte) 0x0e, (byte) 0x0b, (byte) 0x0d},
            {(byte) 0x0d, (byte) 0x09, (byte) 0x0e, (byte) 0x0b},
            {(byte) 0x0b, (byte) 0x0d, (byte) 0x09, (byte) 0x0e}
    };

    public AES(String key) {
        rounds = new ArrayList<>();
        roundKeys = new String[11][4];
        String hexKey = stringToHex(key);
        KeyExpansion.keyExpansion(hexKey, roundKeys, true);
    }





    public String encrypt(String plaintext) {
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i += 16) {
            String block = plaintext.substring(i, Math.min(i + 16, plaintext.length()));
            String paddedBlock = padString(block);
            String hexPlaintext = stringToHex(paddedBlock);
            initializeState(hexPlaintext);
            System.out.println("Initial State:");
            addRoundToRounds("Initial State:", state);
            printState();

            printRoundKey(0);
            addRoundKey(0);
            System.out.println("After AddRoundKey(0):");
            addRoundToRounds("After AddRoundKey(0):", state);
            printState();
            for (int round = 1; round <= 9; round++) {
                subBytes(true);
                System.out.println("After SubBytes(" + round + "):");
                addRoundToRounds("After SubBytes(" + round + "):", state);
                printState();

                shiftRows(true);
                System.out.println("After ShiftRows(" + round + "):");
                addRoundToRounds("After ShiftRows(" + round + "):", state);
                printState();

                mixColumns(true);
                System.out.println("After MixColumns(" + round + "):");
                addRoundToRounds("After MixColumns(" + round + "):", state);
                printState();

                printRoundKey(round);
                addRoundKey(round);
                System.out.println("After AddRoundKey(" + round + "):");
                addRoundToRounds("After AddRoundKey(" + round + "):", state);
                printState();
            }
            subBytes(true);
            System.out.println("After SubBytes(10):");
            addRoundToRounds("After SubBytes(10):", state);
            printState();

            shiftRows(true);
            System.out.println("After ShiftRows(10):");
            addRoundToRounds("After ShiftRows(10):", state);
            printState();

            printRoundKey(10);
            addRoundKey(10);
            System.out.println("After AddRoundKey(10):");
            addRoundToRounds("After AddRoundKey(10):", state);
            printState();
            ciphertext.append(getStateAsString());
        }
        return hexToString(ciphertext.toString());
    }


    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 16) {
            String block = ciphertext.substring(i, Math.min(i + 16, ciphertext.length()));
            String paddedBlock = padString(block);
            String hexCiphertext = stringToHex(paddedBlock);

            initializeState(hexCiphertext);
            System.out.println("Initial State:");
            addRoundToRounds("Initial State:", state);
            printState();

            printRoundKey(10);
            addRoundKey(10);
            System.out.println("After AddRoundKey(10):");
            addRoundToRounds("After AddRoundKey(10):", state);
            printState();
            for (int round = 9; round >= 1; round--) {
                shiftRows(false);
                System.out.println("After InvShiftRows(" + round + "):");
                addRoundToRounds("After InvShiftRows(" + round + "):", state);
                printState();

                subBytes(false);
                System.out.println("After InvSubBytes(" + round + "):");
                addRoundToRounds("After InvSubBytes(" + round + "):", state);
                printState();

                printRoundKey(round);
                addRoundKey(round);
                System.out.println("After AddRoundKey(" + round + "):");
                addRoundToRounds("After AddRoundKey(" + round + "):", state);
                printState();

                mixColumns(false);
                System.out.println("After InvMixColumns(" + round + "):");
                addRoundToRounds("After InvMixColumns(" + round + "):", state);
                printState();
            }

            shiftRows(false);
            System.out.println("After InvShiftRows(0):");
            addRoundToRounds("After InvShiftRows(0):", state);
            printState();

            subBytes(false);
            System.out.println("After InvSubBytes(0):");
            addRoundToRounds("After InvSubBytes(0):", state);
            printState();

            printRoundKey(0);
            addRoundKey(0);
            System.out.println("After AddRoundKey(0):");
            addRoundToRounds("After AddRoundKey(0):", state);
            printState();

            plaintext.append(hexToString(getStateAsString()));
        }
        return unpadString(plaintext.toString());
    }

    private void initializeState(String input) {
        state = new String[4][4];
        for (int i = 0; i < 16; i++) {
            int row = i % 4;
            int col = i / 4;
            state[row][col] = input.substring(i * 2, i * 2 + 2);
        }
    }

    private void subBytes(boolean isEncryption) {
        String[][] sbox = isEncryption ? SBox.SBOX : SBox.INV_SBOX;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String byteStr = state[i][j];
                int row = Integer.parseInt("" + byteStr.charAt(0), 16);
                int col = Integer.parseInt("" + byteStr.charAt(1), 16);
                state[i][j] = sbox[row][col];
            }
        }
    }

    private void shiftRows(boolean isEncryption) {
        for (int i = 1; i < 4; i++) {
            if (isEncryption) {
                state[i] = leftShift(state[i], i);
            } else {
                state[i] = rightShift(state[i], i);
            }
        }
    }

    private String[] leftShift(String[] row, int count) {
        String[] result = new String[4];
        for (int i = 0; i < 4; i++) {
            result[i] = row[(i + count) % 4];
        }
        return result;
    }

    private String[] rightShift(String[] row, int count) {
        String[] result = new String[4];
        for (int i = 0; i < 4; i++) {
            result[i] = row[(i - count + 4) % 4];
        }
        return result;
    }

    private static byte GMul(byte a, byte b) {
        byte p = 0;

        for (int counter = 0; counter < 8; counter++) {
            if ((b & 1) != 0) {
                p ^= a;
            }

            boolean hiBitSet = (a & 0x80) != 0;
            a <<= 1;
            if (hiBitSet) {
                a ^= 0x1B; // x^8 + x^4 + x^3 + x + 1
            }
            b >>= 1;
        }

        return p;
    }

    private void mixColumns(boolean isEncryption) {
        byte[][] s = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                s[i][j] = (byte) Integer.parseInt(state[i][j], 16);
            }
        }

        byte[][] matrix = isEncryption ? MIX_COLUMNS_MATRIX : INV_MIX_COLUMNS_MATRIX;
        byte[][] newMatrix = new byte[4][4];

        for (int c = 0; c < 4; c++) {
            newMatrix[0][c] = (byte) (GMul(matrix[0][0], s[0][c]) ^ GMul(matrix[0][1], s[1][c]) ^ GMul(matrix[0][2], s[2][c]) ^ GMul(matrix[0][3], s[3][c]));
            newMatrix[1][c] = (byte) (GMul(matrix[1][0], s[0][c]) ^ GMul(matrix[1][1], s[1][c]) ^ GMul(matrix[1][2], s[2][c]) ^ GMul(matrix[1][3], s[3][c]));
            newMatrix[2][c] = (byte) (GMul(matrix[2][0], s[0][c]) ^ GMul(matrix[2][1], s[1][c]) ^ GMul(matrix[2][2], s[2][c]) ^ GMul(matrix[2][3], s[3][c]));
            newMatrix[3][c] = (byte) (GMul(matrix[3][0], s[0][c]) ^ GMul(matrix[3][1], s[1][c]) ^ GMul(matrix[3][2], s[2][c]) ^ GMul(matrix[3][3], s[3][c]));
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = String.format("%02x", newMatrix[i][j]);
            }
        }
    }
    private void addRoundKey(int round) {
        for (int i = 0; i < 4; i++) {
            String word = roundKeys[round][i];
            for (int j = 0; j < 4; j++) {
                state[j][i] = xorHex(state[j][i], word.substring(j * 2, j * 2 + 2));
            }
        }
    }

    private String xorHex(String a, String b) {
        int intA = Integer.parseInt(a, 16);
        int intB = Integer.parseInt(b, 16);
        return String.format("%02x", intA ^ intB);
    }

    private String getStateAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                sb.append(state[j][i]);
            }
        }
        return sb.toString();
    }


    private void printState() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printRoundKey(int round) {
        System.out.println("Round Key " + round + ":");

        for (int i = 0; i < 4; i++) {
            String hexKey = roundKeys[round][i];
            System.out.print(hexKey + " ");

            // Convert the hexadecimal key to decimal and print
            StringBuilder decimalRepresentation = new StringBuilder();
            for (int j = 0; j < hexKey.length(); j += 2) {
                String byteHex = hexKey.substring(j, j + 2);
                int byteValue = Integer.parseInt(byteHex, 16);
                decimalRepresentation.append(byteValue).append(" ");
            }
            System.out.print("(" + decimalRepresentation.toString().trim() + ")");
            System.out.println();
        }
        System.out.println();
    }

    private String stringToHex(String input) {
        StringBuilder hex = new StringBuilder();
        for (char c : input.toCharArray()) {
            hex.append(String.format("%02x", (int) c));
        }
        return hex.toString();
    }

    private String hexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    private String padString(String input) {
        int length = input.length();
        int padLength = 16 - (length % 16);
        return input + new String(new char[padLength]).replace('\0', '0');
    }

    private String unpadString(String input) {
        int end = input.length();
        while (end > 0 && input.charAt(end - 1) == '0') {
            end--;
        }
        return input.substring(0, end);

    }
    private void addRoundToRounds(String description, String[][] state) {
        StringBuilder sb = new StringBuilder(description + "\n");
        for (String[] row : state) {
            for (String s : row) {
                sb.append(s).append(" ");
            }
            sb.append("\n");
        }
        rounds.add(sb.toString());
    }

    public List<String> getRounds() {
        return rounds;
    }

    public static void main(String[] args) {
        String key = "Thats my Kung Fu";
        String plaintext = "12";
        String pplaintext = "Two One Nine Two";
//        String ciphertext = ")ÃP_W\u0014 ö@\"\u0099³\u001A\u0002×:";
//        String ciphertext = ")ÃP_W ö@\"\u0099³×:";

        AES aes = new AES(key);
        System.out.println("Key: " + key);
        String ciphertext = aes.encrypt(plaintext);
        System.out.println("Ciphertext: " + ciphertext);
//        String decrypted = aes.decrypt(ciphertext);
//        System.out.println("Decrypted: " + decrypted);
    }
}
