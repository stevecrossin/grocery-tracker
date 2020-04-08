package com.stevecrossin.grocerytracker.utils;

public class ColesReceiptItem {

    public static final String ORDER_SUMMARY_KEYWORD = "ORDER SUMMARY";
    public static final String QUANTITY_PRICE_KEYWORD = "Quantity Price";
    public static final String SO_ON_KEYWORD ="…";
    public static final String TOTAL_KEYWORD ="Estimated total";
//    public static final String TOTAL_SAVING_KEYWORD = "You've saved";
    public static final String PER_KEYWORD = "per";
    public static final String PRICE_UNIT_KEYWORD = "$";
    public static final int EXPECTED_ITEM__SECTION_LINE = 5;

    public static boolean Is_Quantity_Price_Keyword(String line)
    {
        return line.equals(QUANTITY_PRICE_KEYWORD) ;
    }

    public static boolean Is_Order_Summary_Keyword(String line)
    {
        return line.equals(ORDER_SUMMARY_KEYWORD) ;
    }

//    public static boolean Is_Total_Keyword(String line)
//    {
//        return line.equals(TOTAL_KEYWORD) ;
//    }

    public static boolean Is_Empty(String line)
    {
        return (line.trim().length() ==0);
    }

    public static boolean Is_So_On_Keyword(String line)
    {
        return line.equals(SO_ON_KEYWORD) ;
    }

    public static boolean Is_Total_Line(String line)
    {
        return line.startsWith(TOTAL_KEYWORD);
    }

//    public static boolean Is_Total_Saving_Line(String line)
//    {
//        return line.startsWith(TOTAL_SAVING_KEYWORD);
//    }

    public static boolean Is_Per_Unit_Price_Line(String line)
    {
        return line.contains(PER_KEYWORD);
    }

    public static boolean Is_Price(String line)
    {
        return line.startsWith(PRICE_UNIT_KEYWORD);
    }

    public static boolean Is_Quantity(String line)
    {
        for (int i=0; i<line.length();i++) {
            if (!Character.isDigit(line.charAt(i))) return false;
        }
            return true;
    }
}
