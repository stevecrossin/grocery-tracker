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

    private String removeHeaderAndFooter(String parsedText) {
        receipt_Header = parsedText.substring(0, parsedText.lastIndexOf(WoolworthsReceiptItem.PRICE_KEYWORD));
        receipt_Footer = parsedText.substring(parsedText.lastIndexOf(WoolworthsReceiptItem.SUBTOTAL_KEYWORD));
        String contentText = parsedText.substring(parsedText.lastIndexOf(WoolworthsReceiptItem.PRICE_KEYWORD) + WoolworthsReceiptItem.PRICE_KEYWORD.length()
                , parsedText.lastIndexOf(WoolworthsReceiptItem.SUBTOTAL_KEYWORD));
        return contentText;
    }

    private ArrayList<String> removeEmptyLines() {
        ArrayList<String> order_content = new ArrayList<>();
        for (String line : receipt_lines) {
            if (ColesReceiptItem.Is_Empty(line))
                continue;
            else order_content.add(line);
        }
        return order_content;
    }

    public List<ReceiptLineItem> Parse() {
        List<ReceiptLineItem> receiptLineItems = new ArrayList<>();

        ArrayList<String> order_content = removeEmptyLines();

        int currentLineIndex = order_content.size();
        boolean firstLineOfItems = false;
        String itemName ="";
        String lineItem="";
        while (currentLineIndex>0)
        {
            currentLineIndex--;
            String line = order_content.get(currentLineIndex);
            if (WoolworthsReceiptItem.isOnlyIntegers(line))
            {
                    itemName = (order_content.get(currentLineIndex-1)+ " " + itemName).trim();
                    firstLineOfItems = true;
                    currentLineIndex--;
                    lineItem = itemName + " " + line;
            }
            else if (WoolworthsReceiptItem.endsWithFloat(line))
            {
                firstLineOfItems = true;
                if (itemName.length()>0) lineItem = resolvedDifferentLinesItemName(itemName,line);
                else lineItem = line;

            }
            else
            {
                itemName = itemName.length()>0 ? line+" " + itemName : line;
            }

            if (firstLineOfItems)
            {
                receiptLineItems.add(0,parseItem(lineItem));
                firstLineOfItems = false;
                itemName="";
            }

        }
        return receiptLineItems;

    }

    private String resolvedDifferentLinesItemName(String itemsName, String line)
    {
        String itemDescription="";
        String[] items = line.split(" ");
        for (int i = 0; i < items.length - 3; i++) {
            itemDescription += " "+items[i];
        }

        itemDescription += " " + itemsName;

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
