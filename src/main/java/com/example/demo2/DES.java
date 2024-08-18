package com.example.demo2;

import java.util.ArrayList;
import java.util.List;

public class DES {
    private static final int[][][] S = {
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final int[] IP_INV = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private static final int[] E = {
            32, 1, 2, 3, 4, 5, 4, 5,
            6, 7, 8, 9, 8, 9, 10, 11,
            12, 13, 12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21, 20, 21,
            22, 23, 24, 25, 24, 25, 26, 27,
            28, 29, 28, 29, 30, 31, 32, 1
    };

    private static final int[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] PC2 = {
            14, 17, 11, 24, 1, 5, 3, 28,
            15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56,
            34, 53, 46, 42, 50, 36, 29, 32
    };

    private static final int[] SHIFTS = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    private final List<String> rounds = new ArrayList<>();
    private String key;
    private String inputText;
    private String outputText;

    public List<String> getRounds() {
        return rounds;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getOutputText() {
        return outputText;
    }

    public void encDES() {
        rounds.clear();
        String binaryKey = convertToBinary(key);
        if (binaryKey.length() > 64) {
            binaryKey = binaryKey.substring(0,64);
        }else if (binaryKey.length() < 64) {
            binaryKey = String.format("%64s", binaryKey).replace(' ', '0');
        }
        System.out.println("Key: " + binaryToText(binaryKey));
        String binaryInput = convertToBinary(inputText);
        int padding = 64 - (binaryInput.length()%64);
        if (padding<64){
            binaryInput = String.format("%-" + (binaryInput.length() + padding) + "s", binaryInput).replace(' ', '0');
            }
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < binaryInput.length(); i += 64) {
            String block = binaryInput.substring(i, Math.min(i + 64, binaryInput.length()));

            if (block.length() < 64) {
                block = String.format("%64s", block).replace(' ', '0');
            }

            block = permute(block, IP);
            System.out.println("Initial Permutation: " + binaryToHex(block));

            String left = block.substring(0, 32);
            String right = block.substring(32, 64);

            String[] roundKeys = generateRoundKeys(binaryKey);

            for (int j = 0; j < 16; j++) {
                String rightExpanded = expand(right);
                String xorResult = xor(rightExpanded, roundKeys[j]);
                String sBoxResult = sBoxTransform(xorResult);
                String pResult = permute(sBoxResult, P);

                String newRight = xor(left, pResult);
                left = right;
                right = newRight;

                System.out.printf("Round %2d: Left = %s, Right = %s, Round Key = %s%n",
                        j + 1, binaryToHex(left), binaryToHex(right), binaryToHex(roundKeys[j]));
                rounds.add("Round " + (j + 1) + ": Left = " + binaryToHex(left) + ", Right = "
                        + binaryToHex(right) + ", Round Key = " + binaryToHex(roundKeys[j]));
            }

            String preOutput = right + left;
            System.out.println("After Combination: " + binaryToHex(preOutput));
            encryptedText.append(binaryToText(permute(preOutput, IP_INV)));
        }

        outputText =encryptedText.toString();
    }
    public void decDES() {
        rounds.clear();
        String binaryKey = convertToBinary(key);
        if (binaryKey.length() > 64) {
            binaryKey = binaryKey.substring(0,64);
        }else if (binaryKey.length() < 64) {
            binaryKey = String.format("%64s", binaryKey).replace(' ', '0');
        }
        System.out.println("Key: " + binaryToText(binaryKey));

        String binaryInput = textToBinary(inputText);
        int padding = 64 - (binaryInput.length()%64);
        if (padding<64){
            binaryInput = String.format("%-" + (binaryInput.length() + padding) + "s", binaryInput).replace(' ', '0');
        }
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < binaryInput.length(); i += 64) {
            String block = binaryInput.substring(i, Math.min(i + 64, binaryInput.length()));

            if (block.length() < 64) {
                block = String.format("%64s", block).replace(' ', '0');
            }

            block = permute(block, IP);
            System.out.println("Initial Permutation: " + binaryToHex(block));

            String left = block.substring(0, 32);
            String right = block.substring(32, 64);

            String[] roundKeys = generateRoundKeys(binaryKey);

            for (int j = 15; j >= 0; j--) {
                String rightExpanded = expand(right);
                String xorResult = xor(rightExpanded, roundKeys[j]);
                String sBoxResult = sBoxTransform(xorResult);
                String pResult = permute(sBoxResult, P);

                String newRight = xor(left, pResult);
                left = right;
                right = newRight;

                System.out.printf("Round %2d: Left = %s, Right = %s, Round Key = %s%n",
                        16 - j, binaryToHex(left), binaryToHex(right), binaryToHex(roundKeys[j]));
                rounds.add("Round " + (16 - j) + ": Left = " + binaryToHex(left) + ", Right = "
                        + binaryToHex(right) + ", Round Key = " + binaryToHex(roundKeys[j]));
            }

            String preOutput = right + left;
            System.out.println("After Combination: " + binaryToHex(preOutput));
            decryptedText.append(binaryToText(permute(preOutput, IP_INV)));
        }

        outputText = decryptedText.toString();
    }
    private String permute(String input, int[] table) {
        StringBuilder output = new StringBuilder();
        for (int i : table) {
            output.append(input.charAt(i - 1));
        }
        return output.toString();
    }

    private String expand(String input) {
        return permute(input, E);
    }

    private String sBoxTransform(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            String block = input.substring(i * 6, (i + 1) * 6);
            int row = Integer.parseInt(String.valueOf(block.charAt(0)) + block.charAt(5), 2);
            int col = Integer.parseInt(block.substring(1, 5), 2);
            int sBoxValue = S[i][row][col];
            output.append(String.format("%4s", Integer.toBinaryString(sBoxValue)).replace(' ', '0'));
        }
        return output.toString();
    }

    private String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) ^ b.charAt(i));
        }
        return result.toString();
    }

    private static int[] circular_left_shift(int[] a, int j) {
        int[] b = new int[a.length];
        int index;
        for (int i = 0; i < a.length; i++) {
            index = (i - j) % a.length;
            if (index < 0) {
                index += a.length;
            }
            b[index] = a[i];
        }
        return b;
    }

    private String[] generateRoundKeys(String key) {
        String[] roundKeys = new String[16];
        key = permute(key, PC1);

        int[] left = key.substring(0, 28).chars().map(c -> c - '0').toArray();
        int[] right = key.substring(28, 56).chars().map(c -> c - '0').toArray();

        for (int i = 0; i < 16; i++) {
            left = circular_left_shift(left, SHIFTS[i]);
            right = circular_left_shift(right, SHIFTS[i]);
            roundKeys[i] = permute(arrayToString(left) + arrayToString(right), PC2);
        }

        return roundKeys;
    }

    private String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int i : array) {
            sb.append(i);
        }
        return sb.toString();
    }

    private String hexToBinary(String hex) {
        StringBuilder binary = new StringBuilder();
        for (char c : hex.toCharArray()) {
            binary.append(String.format("%4s", Integer.toBinaryString(Integer.parseInt(Character.toString(c), 16))).replace(' ', '0'));
        }
        return binary.toString();
    }

    private String binaryToHex(String binary) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 4) {
            hex.append(Integer.toHexString(Integer.parseInt(binary.substring(i, i + 4), 2)));
        }
        return hex.toString().toUpperCase();
    }



    private String convertToBinary(String input) {
        if (input.matches("[0-9A-Fa-f]+") && input.length() % 2 == 0) {
            return hexToBinary(input);
        } else {
            return textToBinary(input);
        }
    }

    private static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char character : text.toCharArray()) {
            binary.append(
                    String.format("%8s", Integer.toBinaryString(character))
                            .replaceAll(" ", "0")
            );
        }
        return binary.toString();
    }

    private static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length(); i += 8) {
            String byteString = binary.substring(i, i + 8);
            char character = (char) Integer.parseInt(byteString, 2);
            text.append(character);
        }
        return text.toString();
    }
    private String textToHex(String text) {
        StringBuilder hex = new StringBuilder();
        for (char character : text.toCharArray()) {
            hex.append(String.format("%02X", (int) character));
        }
        return hex.toString();
    }

    private String hexToText(String hex) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            text.append((char) Integer.parseInt(str, 16));
        }
        return text.toString();
    }

    public static void main(String[] args) {
//        DES des = new DES();
//        des.setKey("AABB09182736CCDD");
//        des.setInputText("123456ABCD132536");
//        des.encDES();
//        System.out.println("Encrypted: " + des.getOutputText());
//        des.setInputText(des.getOutputText());
//        des.decDES();
//        System.out.println("Decrypted: " + des.getOutputText());
        String x = "Mohamed Sayed";
        x = textToBinary(x);
        System.out.println(x);
        String y = binaryToText(x);
        System.out.println(y);
    }
}
