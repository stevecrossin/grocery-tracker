package com.stevecrossin.grocerytracker.models;

import com.stevecrossin.grocerytracker.utils.ColesReceiptItem;
import com.stevecrossin.grocerytracker.utils.WoolworthsReceiptItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WoolworthsReceipt {
    String receipt_Header;
    String receipt_Footer;
    String[] receipt_lines;

    public WoolworthsReceipt(String parsedText) {
        receipt_lines = removeHeaderAndFooter(parsedText).split("\n");
    }

    /**
     * The method is used to remove all un-necessary part of the receipt and only keep the items details
     * @param parsedText :full text of receipt
     * @return : the text after remove the header and footer, only keep the content related to items
     */
    private String removeHeaderAndFooter(String parsedText) {
        receipt_Header = parsedText.substring(0, parsedText.lastIndexOf(WoolworthsReceiptItem.PRICE_KEYWORD));
        receipt_Footer = parsedText.substring(parsedText.lastIndexOf(WoolworthsReceiptItem.SUBTOTAL_KEYWORD));
        String contentText = parsedText.substring(parsedText.lastIndexOf(WoolworthsReceiptItem.PRICE_KEYWORD) + WoolworthsReceiptItem.PRICE_KEYWORD.length()
                , parsedText.lastIndexOf(WoolworthsReceiptItem.SUBTOTAL_KEYWORD));
        return contentText;
    }

    /**
     * the method is used to remove all empty lines
     * @return The array list without out empty lines (space only line)
     */
    private ArrayList<String> removeEmptyLines() {
        ArrayList<String> order_content = new ArrayList<>();
        for (String line : receipt_lines) {
            if (ColesReceiptItem.Is_Empty(line))
                continue;
            else order_content.add(line);
        }
        return order_content;
    }


    /**
     * The method is used to parse the text
     * Assumption: The start of the item's description (the beginning of each item) should be in the same line with or in the line before the price/quantity/unit price
     * In another words: if quantity/price/unit price is in line n then the begining of each item should be in line (n-1) or n
     * The parse concept follows those steps:
     *      Remove all empty (only space) lines
     *      Start from the end of the receipt
     *          if the line contains only number -> only quantity/price/unit line (1)
     *          if the line ends with float number -> include item's description and quantity/price/unit (2)
     *          else the line is one part of the item's description so we will store in order to re-connect to the full description
     *      For the (1) and (2) cases, if there existed the "part of the item's description" so we execute the resolvedDifferentLinesItemName function
     *      to re-connect them
     *      Parse the full description and quantity/price/unit price to the ReceiptLineItem
     * @return the full description and quantity/price/unit price of all items
     */
    public List<ReceiptLineItem> Parse() {
        List<ReceiptLineItem> receiptLineItems = new ArrayList<>();

        // Remove all empty lines
        ArrayList<String> order_content = removeEmptyLines();

        // initialize current line as the bottom of the array
        int currentLineIndex = order_content.size();

        // boolean variable to identify whether the current line is the start of items
        boolean firstLineOfItems = false;

        // String variable to store the external parts of description/name of items
        String itemName ="";

        // String variable to store the description, quantity/price/unit price for the parsing to ReceiptLineItem
        String lineItem="";

        // Start from bottom -> 0
        while (currentLineIndex>0)
        {
            currentLineIndex--;
            String line = order_content.get(currentLineIndex);

            // The line is only number -> quantity/ price/unit price line -> the (currentline -1) is the first line of item
            // if there exists itemName value then re-connect the full description for the item
            if (WoolworthsReceiptItem.isOnlyIntegers(line))
            {
                    itemName = (order_content.get(currentLineIndex-1)+ " " + itemName).trim();
                    firstLineOfItems = true;
                    currentLineIndex--;
                    lineItem = itemName + " " + line;
            }
            // The line includes the description and quantity/price/unit price -> current line is the first line of item
            // if there exists itemName value then re-connect the full description for the item
            else if (WoolworthsReceiptItem.endsWithFloat(line))
            {
                firstLineOfItems = true;
                if (itemName.length()>0) lineItem = resolvedDifferentLinesItemName(itemName,line);
                else lineItem = line;

            }
            // The line is one part of the item's description -> store into the itemName variable
            else
            {
                itemName = itemName.length()>0 ? line+" " + itemName : line;
            }

            // if we are in the first line of item which means we already collect all needed information about the items
            // so now we could parse to LineItems Obj
            if (firstLineOfItems)
            {
                receiptLineItems.add(0,parseItem(lineItem));
                firstLineOfItems = false;
                itemName="";
            }

        }
        return receiptLineItems;

    }

    /**
     * The method is used to re-connect item's description parts which are located in different lines
     * @param itemsName : the name collect from different lines
     * @param line : the first line of the items which is includes description , quantity/price/unit price
     * @return the proper line follows order :full description -> price /quantity/ unit price
     */
    private String resolvedDifferentLinesItemName(String itemsName, String line)
    {
        String itemDescription="";
        String[] items = line.split(" ");

        // As the last 3 items are price/quantity/unit price so all before are the item's description
        for (int i = 0; i < items.length - 3; i++) {
            itemDescription += " "+items[i];
        }

        // add the extra parts (collects from other lines) to the items description
        itemDescription += " " + itemsName;

        // add price/quantity/ unit price at the end
        for (int i = items.length - 3; i < items.length; i++) {
            itemDescription += " " + items[i];
        }

        return itemDescription.trim();
    }

    /**
     * Parses the items in the receipt
     *
     * @param lineItem
     * @return receiptLineItem
     */
    private ReceiptLineItem parseItem(String lineItem) {
        // Split by space character.
        String[] columns = lineItem.split(" ");
        ReceiptLineItem receiptLineItem = new ReceiptLineItem();
        // The last three entries will be unit price, quantity and price.
        receiptLineItem.price = Float.parseFloat(columns[columns.length - 1]);
        receiptLineItem.quantity = Float.parseFloat(columns[columns.length - 2]);
        receiptLineItem.unitPrice = Float.parseFloat(columns[columns.length - 3]);

        // The rest of the bits will form the item name, so piece them all together before
        // putting them in ReceiptLineItem.
        receiptLineItem.itemDescription = "";
        for (int i = 0; i < columns.length - 3; i++) {
            receiptLineItem.itemDescription = receiptLineItem.itemDescription + " ";
            receiptLineItem.itemDescription = receiptLineItem.itemDescription + columns[i];
        }
        receiptLineItem.itemDescription = receiptLineItem.itemDescription.trim();
        return receiptLineItem;
    }
}
