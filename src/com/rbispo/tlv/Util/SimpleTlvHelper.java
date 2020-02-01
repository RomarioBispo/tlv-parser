package com.rbispo.tlv.Util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que codifica/decodifica mensagem no formato Simple TLV.
 *
 */
public class SimpleTlvHelper {

    /**
     * Reads TLV values for a given hex string.
     */
    public static byte[][] readTLV(String tlvHexString, int tag) {
        return readTLV(hexStringToByteArray(tlvHexString), tag);
    }

    /**
     * Reads TLV values for a given byte array.
     */
    public static byte[][] readTLV(byte[] tlv, int tag) {
        if (tlv == null || tlv.length < 1) {
            throw new IllegalArgumentException("Invalid TLV");
        }

        int c = 0;
        ArrayList al = new ArrayList();

        ByteArrayInputStream is = null;
        try {
            is = new ByteArrayInputStream(tlv);

            while ((c = is.read()) != -1) {
                if (c == tag) {
                    System.out.println("Got tag");
                    if ((c = is.read()) != -1) {
                        byte[] value = new byte[c];
                        is.read(value, 0, c);
                        al.add(value);
                    }
                }
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Got " + al.size() + " values for tag "
                + Integer.toHexString(tag));
        byte[][] vals = new byte[al.size()][];
        al.toArray(vals);
        return vals;
    }

    /**
     * Converts a hex string to byte array.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }

    public static byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }

    private static int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }

    public static String encodeHexString(byte[] byteArray) {
        StringBuffer hexStringBuffer = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            hexStringBuffer.append(byteToHex(byteArray[i]));
        }
        return hexStringBuffer.toString();
    }

    public static byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }
}
