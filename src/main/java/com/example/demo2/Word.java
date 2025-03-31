package com.example.demo2;

public class Word {
    String[] bytes;

    public Word() {
        bytes = new String[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = "00";
        }
    }

    public Word(String b0, String b1, String b2, String b3) {
        bytes = new String[] { b0, b1, b2, b3 };
    }
}
