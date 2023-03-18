package com.example.cryptochat.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CryptoController {

    // Const values
    private static final List<Integer> swapingKey = Arrays.asList(1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1);
    private static final List<Integer> swapingPhase = Arrays.asList(1, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 1, 0, 1);
    private static final List<Integer> invertingKey = Arrays.asList(1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 1);
    private static final List<Integer> invertingPhase = Arrays.asList(1, 1, 1, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 0, 1);
    // Const binary sequences(write to file if needed)
    private static final String swapingSeq = stringArrayToString(formBinarySequenceCode(swapingKey,
            swapingPhase, 16, 2, 624));
    private static final String invertingSeq = stringArrayToString(formBinarySequenceCode(invertingKey,
            invertingPhase, 16, 2, 624));


    ////////// IVAN STEPANCHENKO //////////
    public static String encrypt(String textMessages, String password) {
        int[] key = convertBinaryStringToIntArray(finalCryptoCode(formSequenceCode(password)));
        int[] text = stringToBinary(textMessages);
        int[] result = new int[text.length];
        for (int i = 0; i < text.length; i++) {
            int charCode = text[i];
            int keyInt = key[i % key.length];
            int encryptedChar = charCode ^ keyInt;
            int encryptedCharCode = Integer.parseInt(Integer.toBinaryString(encryptedChar), 2);
            result[i] = encryptedCharCode;
        }
        return binaryToString(result);
    }

    ///////////// VOVA TRUBIN///////////////
    public static String finalCryptoCode(List<String[]> sequenceCode) {
        StringBuffer result = new StringBuffer();
        StringBuffer temp = new StringBuffer();
        int finalSequenceLength = swapingSeq.length();
        for (int i = 0; i < finalSequenceLength; i++) {
            temp.setLength(0);
            // Swap if needed
            if (swapingSeq.charAt(i) == '0') {
                temp.append(sequenceCode.get(0)[i] + sequenceCode.get(1)[i]);
            } else {
                temp.append(sequenceCode.get(1)[i] + sequenceCode.get(0)[i]);
            }
            // Invert if needed and add value to result
            if (invertingSeq.charAt(i) == '0') {
                result.append(temp);
            } else {
                invertBits(temp);
                result.append(temp);
            }

        }
        return result.toString();
    }

    private static void invertBits(StringBuffer bits) {
        int bitsLength = bits.length(); // = 8
        for (int i = 0; i < bitsLength; i++) {
            char c = bits.charAt(i);
            if (c == '0') {
                bits.setCharAt(i, '1');
            } else if (c == '1') {
                bits.setCharAt(i, '0');
            }
        }
    }


    public static String[] formBinarySequenceCode(List<Integer> key, List<Integer> phase, int power, int base, int codeLength) {
        String[] sequenceCode = new String[codeLength];
        for (int i = 0; i < power; i++) {
            sequenceCode[i] = String.valueOf(phase.get(i));
        }
        List<Integer> fragment = new ArrayList<>(phase);
        int fragmentIndex = power - 1;
        for (int i = power; i < codeLength; i++) {
            fragment = formNextFragment(key, fragment, base, power);
            sequenceCode[i] = fragment.get(fragmentIndex).toString();
        }
        return sequenceCode;
    }

    public static String stringArrayToString(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s);
        }
        return sb.toString();
    }

    ///////////// IVAN STEPANCHENKO ////////////////
    public static List<String[]> formSequenceCode(String password) {
        List<Integer[]> phases = makeTwoPhase(makeFirstPhase(password));
        List<String[]> result = new ArrayList<>();
        result.add(new String[625]);
        result.add(new String[6859]);
        int[] bases = {5, 19};
        int[] bitCount = {3, 5};
        List<List<Integer>> keys = new ArrayList<>();
        keys.add(Arrays.asList(2, 4, 4, 4));
        keys.add(Arrays.asList(10, 17, 8));
        int[] power = {4, 3};
        for (int r = 0; r < 2; r++) {
            List<Integer> fragment = new ArrayList<>(Arrays.asList(phases.get(r)));
            int fragmentIndex = power[r] - 1;
            for (int i = 0; i < result.get(r).length; i++) {
                fragment = formNextFragment(keys.get(r), fragment, bases[r], power[r]);
                result.get(r)[i] = decimalToBinaryWithBitCount(fragment.get(fragmentIndex), bitCount[r]);
            }
        }
        return result;
    }

    private static List<Integer> formNextFragment(List<Integer> key, List<Integer> fragment, int base, int keyLength) {
        List<Integer> nextFragment = new ArrayList<>();
        int newValue;
        int counter = 0;
        for (int i = 0; i < keyLength; i++) {
            counter += fragment.get(i) * key.get(i);
        }
        newValue = counter % base;
        for (int i = 0; i < keyLength - 1; i++) {
            nextFragment.add(fragment.get(i + 1));
        }
        nextFragment.add(newValue);
        return nextFragment;
    }

    private static List<Integer[]> makeTwoPhase(int[] firstPhase) {
        List<Integer[]> l = new ArrayList<>();
        Integer[] phaseWithBaseFive = new Integer[4];
        Integer[] phaseWithBaseNineteen = new Integer[3];
        int[] arrayNum3 = new int[3];
        int[] arrayNum5 = new int[5];
        int c1 = 0, c2 = 0, p1 = 0, p2 = 0;
        for (int i = 0; i < 27; i++) {
            if (i < 12) {
                arrayNum3[p1++] = firstPhase[i];
                if (p1 == 3) {
                    if (binaryArrayToDecimal(arrayNum3) > 4) {
                        bitwiseInvert(arrayNum3);
                    }
                    phaseWithBaseFive[c1++] = binaryArrayToDecimal(arrayNum3);
                    p1 = 0;
                }
            }
            if (i > 11) {
                arrayNum5[p2++] = firstPhase[i];
                if (p2 == 5) {
                    if (binaryArrayToDecimal(arrayNum5) > 18) {
                        bitwiseInvert(arrayNum5);
                    }
                    phaseWithBaseNineteen[c2++] = binaryArrayToDecimal(arrayNum5);
                    p2 = 0;
                }
            }

        }
        l.add(phaseWithBaseFive);
        l.add(phaseWithBaseNineteen);
        return l;
    }

    private static String decimalToBinaryWithBitCount(int decimal, int bitCount) {
        StringBuilder binary = new StringBuilder();
        while (decimal > 0) {
            int remainder = decimal % 2;
            binary.insert(0, remainder);
            decimal = decimal / 2;
        }
        int leadingZeros = bitCount - binary.length();
        if (leadingZeros > 0) {
            binary.insert(0, "0".repeat(leadingZeros));
        }
        return binary.toString();
    }

    private static void bitwiseInvert(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] ^ 1;
        }
    }

    private static int binaryArrayToDecimal(int[] binaryArray) {
        int decimal = 0;
        int power = 0;
        for (int i = binaryArray.length - 1; i >= 0; i--) {
            if (binaryArray[i] == 1) {
                decimal += Math.pow(2, power);
            }
            power++;
        }
        return decimal;
    }

    public static int[] convertBinaryStringToIntArray(String binaryString) {
        int[] result = new int[binaryString.length()];
        for (int i = 0; i < binaryString.length(); i++) {
            result[i] = Character.getNumericValue(binaryString.charAt(i));
        }
        return result;
    }



    ////////// VLAD RADKO //////////
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

    ///////////// HARD ENCRYPT //////////////

    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            char c = (char) (random.nextInt(100));
            sb.append(c);
        }
        return sb.toString();
    }

    public static String formNewPassword(String password, String generatedPassword) {
        int[] passwordInt = stringToBinary(password);
        int[] generatedPasswordInt = stringToBinary(generatedPassword);
        int[] result = new int[generatedPasswordInt.length];
        for (int i = 0; i < generatedPasswordInt.length; i++) {
            int passwordIntCode = passwordInt[i];
            int generatedPasswordIntCode = generatedPasswordInt[i];
            int encryptedChar = passwordIntCode ^ generatedPasswordIntCode;
            int encryptedCharCode = Integer.parseInt(Integer.toBinaryString(encryptedChar), 2);
            result[i] = encryptedCharCode;
        }
        return binaryToString(result);
    }

    public static String encryptHard(String textMessages, String password) {
        String randomString = generateRandomString();
        String newPassword = formNewPassword(password, randomString);
        StringBuilder encryptMessageHard = new StringBuilder();
        String encryptMessage = encrypt(textMessages, newPassword);
        encryptMessageHard.append(randomString);
        encryptMessageHard.append(encryptMessage);
        encryptMessageHard.append("   ");
        return encryptMessageHard.toString();
    }

    public static List<String> extractMessageAndPassword(String hardEncryptedText) {
        List<String> result = new ArrayList<>();
        result.add(hardEncryptedText.substring(0, 16));
        result.add(hardEncryptedText.substring(16, hardEncryptedText.length() - 3));
        return result;
    }

    public static String uncryptHard(String textMessage, String password) {
        return encrypt(extractMessageAndPassword(textMessage).get(1), formNewPassword(password, extractMessageAndPassword(textMessage).get(0)));
    }

    public static String cryptoHard(String textMessage, String password) {
        if (textMessage.endsWith("   ")) {
            return uncryptHard(textMessage, password);
        } else {
            return encryptHard(textMessage, password);
        }
    }
}