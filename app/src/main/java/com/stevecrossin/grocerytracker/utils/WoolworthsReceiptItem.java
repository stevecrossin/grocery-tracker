package com.stevecrossin.grocerytracker.utils;

public class WoolworthsReceiptItem {
    public static final String PRICE_KEYWORD = "Price:";
    public static final String SUBTOTAL_KEYWORD = "Subtotal:";

    /**
     * Returns true if the String ends with a period character followed by 2 integers.
     */
    public static boolean endsWithFloat(String line) {
        return line.matches(".*[+-]?([0-9]*[.])?[0-9]+.$");
    }

    /**
     * Checks if the String consists only integers ignoring space and period character.
     **/
    public static boolean isOnlyIntegers(String line) {
        return line.matches("^[0-9. ]*$");

    }

}
