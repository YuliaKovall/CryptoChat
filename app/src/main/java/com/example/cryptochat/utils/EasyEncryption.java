package com.example.cryptochat.utils;

public class EasyEncryption {
    private static String password;

    public EasyEncryption(String password) {
        EasyEncryption.password = password;
    }

    private static char[] adding(String str, boolean toCrypt) {
        char[] chars = str.toCharArray();
        if (toCrypt) {
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) (chars[i] + 1);
            }
        } else {
            for (int i = 0; i < chars.length; i++) {
                chars[i] = (char) (chars[i] - 1);
            }
        }
        return chars;
    }

    private static void invert(char[] chars) {
        for (int i = 0; i < chars.length / 2; i++) {
            char temp = chars[i];
            chars[i] = chars[chars.length - 1 - i];
            chars[chars.length - 1 - i] = temp;
        }
    }

    public String encrypt(String str) {
        char[] chars;
        if (password.length() % 2 == 0) {
            chars = adding(str, true);
        } else {
            chars = adding(str, false);
        }
        invert(chars);
        return new String(chars);
    }

    public String uncrypt(String str) {
        char[] chars;
        if (password.length() % 2 == 0) {
            chars = adding(str, false);
        } else {
            chars = adding(str, true);
        }
        // знову інвертування масиву символів
        invert(chars);
        // повернення результуючого рядка
        return new String(chars);
    }
}