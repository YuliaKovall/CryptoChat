package com.example.cryptochat.utils;

import java.io.UnsupportedEncodingException;

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
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char) ~(chars[i]);
        }
    }

    public String encrypt(String str) {
        char[] chars = adding(str, password.length() % 2 == 0);
        invert(chars);// використовуємо результат adding() як вхідний параметр для invert()
        return new String(chars);
    }

    public String uncrypt(String str) {
        char[] chars = str.toCharArray();
        invert(chars);// використовуємо вхідний параметр str як вхідний параметр для invert()
        return new String(adding(new String(chars), !(password.length() % 2 == 0)));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Here are methods for obtaining the first 64-bit phase from a password.



    public static int[] stringToBinary(String str) {
        int[] bits = new int[str.length() * 8];
        byte[] bytes = null;
        try {
            bytes = str.getBytes("windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        int bitIndex = 0;
        assert bytes != null;
        for (byte b : bytes) {
            // Convert byte to 8-bit binary representation and store each bit in the bits array
            String binaryByte = Integer.toBinaryString(b & 255 | 256).substring(1);
            for (int i = 0; i < binaryByte.length(); i++) {
                bits[bitIndex++] = Character.getNumericValue(binaryByte.charAt(i));
            }
        }
        return bits;
    }

    public static String binaryToString(int[] bits) {
        if (bits.length % 8 != 0) {
            throw new IllegalArgumentException("The length of the bit array must be a multiple of 8.");
        }

        byte[] bytes = new byte[bits.length / 8];
        int byteIndex = 0;
        int bitIndex = 0;

        while (bitIndex < bits.length) {
            byte b = 0;
            for (int i = 0; i < 8; i++) {
                b <<= 1;
                b |= bits[bitIndex++];
            }
            bytes[byteIndex++] = b;
        }

        String str = null;
        try {
            str = new String(bytes, "windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }

    public static int[] makeFirstPhase(String password) {
        String croppedPass = password.substring(0, 16);
        int[] allBits = stringToBinary(croppedPass);
        int[] bits64 = new int[64];
        int resultIndex = 0;
        for (int i = 4; i < allBits.length; i += 8) {
            bits64[resultIndex++] = allBits[i];
            bits64[resultIndex++] = allBits[i + 1];
            bits64[resultIndex++] = allBits[i + 2];
            bits64[resultIndex++] = allBits[i + 3];
        }
        return bits64;
    }

}