package com.example.cryptochat.utils;

import com.example.cryptochat.controller.CryptoController;

import java.io.UnsupportedEncodingException;

public class EasyEncryption {
    private String password;

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
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ~(chars[i]);
        }
    }

    public String encryptLite(String str) {
        char[] chars = adding(str, password.length() % 2 == 0);
        invert(chars);// використовуємо результат adding() як вхідний параметр для invert()
        return new String(chars);
    }

    public String uncrypt(String str) {
        char[] chars = str.toCharArray();
        invert(chars);// використовуємо вхідний параметр str як вхідний параметр для invert()
        return new String(adding(new String(chars), !(password.length() % 2 == 0)));
    }

    public String encrypt(String message){
        return CryptoController.cryptoHard(message, password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

}