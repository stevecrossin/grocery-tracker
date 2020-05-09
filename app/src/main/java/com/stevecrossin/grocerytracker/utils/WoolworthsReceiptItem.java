package com.stevecrossin.grocerytracker.utils;

public class WoolworthsReceiptItem {
    public static final String PRICE_KEYWORD = "Price:";
    public static final String SUBTOTAL_KEYWORD = "Subtotal:";

    public static boolean endsWithFloat(String line) {
//        char lastChar = line.charAt(line.length() - 1);
//        char lastBeforeChar = line.charAt(line.length() - 2);
//        char periodCharacter = line.charAt(line.length() - 3);
//
//        if (lastChar >= '0' && lastChar <= '9' && lastBeforeChar >= '0' && lastBeforeChar <= '9'
//                && periodCharacter == '.') {
//            return true;
//        }
//        return false;

        return line.matches(".*[+-]?([0-9]*[.])?[0-9]+.$");
    }

    public static boolean isOnlyIntegers(String line) {
//        char[] characters = text.toCharArray();
//        for (char c : characters) {
//            if (c == '.' || c == ' ') {
//                continue;
//            }
//
//            if (c < '0' || c > '9') {
//                return false;
//            }
//        }
//        return true;
        return line.matches("^[0-9. ]*$");

    }

}
