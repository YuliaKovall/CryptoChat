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
    public int[] makeFirstPhase(){
        String croppedPassword = password.substring(0, 16);
        return getAllBinaryArrays(croppedPassword);
    }

    private static int[][] stringToBinaryArrays(String str) {
        int[][] result = new int[str.length()][8];
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            for (int j = 0; j < 8; j++) {
                result[i][j] = (c >> (7 - j)) & 1;
            }
        }
        return result;
    }

    private static int[][] getFourLastElements(int[][] binaryArrays) {
        int[][] result = new int[binaryArrays.length][4];
        for (int i = 0; i < binaryArrays.length; i++) {
            int[] binaryArray = binaryArrays[i];
            int startIndex = binaryArray.length - 4;
            if (startIndex < 0) {
                startIndex = 0;
            }
            int endIndex = binaryArray.length;
            for (int j = startIndex, k = 0; j < endIndex && k < 4; j++, k++) {
                result[i][k] = binaryArray[j];
            }
        }
        return result;
    }

    private static int[] getAllBinaryArrays(String str) {
        int[] result = new int[str.length() * 4];
        int[][] binaryArrays = stringToBinaryArrays(str);
        int[][] lastFourElements = getFourLastElements(binaryArrays);
        int index = 0;
        for (int[] lastFourElement : lastFourElements) {
            for (int i : lastFourElement) {
                result[index++] = i;
            }
        }
        return result;
    }

}