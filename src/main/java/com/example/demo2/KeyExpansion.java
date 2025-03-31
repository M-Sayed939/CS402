package com.example.demo2;

public class KeyExpansion {

    private static final int[] RCON = {
            0x00000000, 0x01000000, 0x02000000, 0x04000000,
            0x08000000, 0x10000000, 0x20000000, 0x40000000,
            0x80000000, 0x1b000000, 0x36000000
    };

    public static void keyExpansion(String key, String[][] roundKeys, boolean isEncryption) {
        String[][] w = new String[44][4];


        for (int i = 0; i < 4; i++) {
            w[i] = new String[] {
                    key.substring(i * 8, i * 8 + 2),
                    key.substring(i * 8 + 2, i * 8 + 4),
                    key.substring(i * 8 + 4, i * 8 + 6),
                    key.substring(i * 8 + 6, i * 8 + 8)
            };
        }


        for (int i = 4; i < 44; i++) {
            String[] temp = w[i - 1].clone();

            if (i % 4 == 0) {
                temp = xorWord(subWord(rotWord(temp), isEncryption), RCON[i / 4]);
            }

            w[i] = new String[4];
            for (int j = 0; j < 4; j++) {
                w[i][j] = xorHex(w[i - 4][j], temp[j]);
            }
        }


        for (int i = 0; i < 44; i++) {
            roundKeys[i / 4][i % 4] = String.join("", w[i]);
        }
    }

    private static String[] rotWord(String[] word) {
        return new String[] { word[1], word[2], word[3], word[0] };
    }

    private static String[] subWord(String[] word, boolean isEncryption) {
        String[][] sbox = isEncryption ? SBox.SBOX : SBox.INV_SBOX;
        String[] subbed = new String[4];
        for (int i = 0; i < 4; i++) {
            int row = Integer.parseInt("" + word[i].charAt(0), 16);
            int col = Integer.parseInt("" + word[i].charAt(1), 16);
            subbed[i] = sbox[row][col];
        }
        return subbed;
    }

    private static String[] xorWord(String[] word, int rcon) {
        String[] result = new String[4];
        result[0] = xorHex(word[0], Integer.toHexString(rcon >>> 24));
        result[1] = xorHex(word[1], "00");
        result[2] = xorHex(word[2], "00");
        result[3] = xorHex(word[3], "00");
        return result;
    }

    private static String xorHex(String a, String b) {
        int intA = Integer.parseInt(a, 16);
        int intB = Integer.parseInt(b, 16);
        return String.format("%02x", intA ^ intB);
    }
}
