package com.example.cryptochat.utils;

public class EasyEncryption {
    private final String password;

    public EasyEncryption(String password) {
        this.password = password;
    }

    private char[] adding(String str, boolean toCrypt) {
        char[] chars = str.toCharArray();
        int offset = toCrypt ? 1 : -1;
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) (chars[i] + offset);
        }
        return chars;
    }

    private void invert(char[] chars) {
        int length = chars.length;
        for (int i = 0; i < length / 2; i++) {
            char temp = chars[i];
            chars[i] = chars[length - 1 - i];
            chars[length - 1 - i] = temp;
        }
    }

    public String encrypt(String str) {
        char[] chars = adding(str, password.length() % 2 == 0);
        invert(chars);
        return new String(chars);
    }

    public String uncrypt(String str) {
        char[] chars = adding(str, password.length() % 2 != 0);
        invert(chars);
        return new String(chars);
    }
}
