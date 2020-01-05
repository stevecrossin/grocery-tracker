package com.stevecrossin.grocerytracker.other;

import java.security.MessageDigest;

/***
 * This code generates a hash for the entered string password. It takes the string, converts to 256 SHA hash, and then converts to a hexadecimal.
 */
public class PasswordScrambler {

    public static String scramblePassword(String data) throws Exception {
        MessageDigest dg = MessageDigest.getInstance("SHA-256");
        dg.update(data.getBytes());
        return convToHexadecimal(dg.digest());
    }

    private static String convToHexadecimal(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes)
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }
}
