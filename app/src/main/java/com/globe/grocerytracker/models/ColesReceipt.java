package com.globe.grocerytracker.models;

import com.globe.grocerytracker.utils.ColesReceiptItem;

import java.util.ArrayList;
import java.util.List;

public class ColesReceipt {

    String receipt_Header;
    String receipt_Footer;
    String[] receipt_lines;

    public ColesReceipt(String parsedText) {
        receipt_lines = parsedText.split("\n");
        IdentifyHeaderAndFooter();
    }

    private void IdentifyHeaderAndFooter() {
        receipt_Header = receipt_lines.length > 0 ? receipt_lines[0] : null;
        receipt_Footer = receipt_lines.length > 1 ? receipt_lines[receipt_lines.length - 1].split(" ")[0] : null;
    }

    // Grab only the order summary part
    private ArrayList<String> GetOrderContent() {
        ArrayList<String> order_content = new ArrayList<>();
        boolean added = false;
        for (String line : receipt_lines) {
            if (IsReceiptHeader(line) || IsReceiptFooter(line) || ColesReceiptItem.Is_So_On_Keyword(line) || ColesReceiptItem.Is_Empty(line))
                continue;
            if (ColesReceiptItem.Is_Total_Line(line)) break;
            if (ColesReceiptItem.Is_Order_Summary_Keyword(line))
                added = true;
            else if (added) order_content.add(line);
        }
        return order_content;
    }

    // Store the position of each category
    private ArrayList<Integer> FindEachCategoryPosition(ArrayList<String> order_content) {
        ArrayList<Integer> categoryPositions = new ArrayList<>();
        for (int i = 0; i < order_content.size(); i++) {
            String line = order_content.get(i);
            if (ColesReceiptItem.Is_Quantity_Price_Keyword(line))
                categoryPositions.add(i);
        }
        categoryPositions.add(order_content.size());

        return categoryPositions;
    }

    // Resolve The Content when multiple information stay in the same line (quantity, price)
    private void resolveInlined(ArrayList<String> order_content) {
        for (int i = 0; i < order_content.size(); i++) {
            String line = order_content.get(i);
            if (ColesReceiptItem.Contain_Price(line)) {
                if (!ColesReceiptItem.Is_Per_Unit_Price_Line(line) && (!ColesReceiptItem.Is_Price(line))) {
                    String[] items = line.split(" ");
                    order_content.remove(i);
                    for (int j = 0; j < items.length; j++)
                        order_content.add(i + j, items[j]);
                }
            }
        }
    }

    public List<ReceiptLineItem> Parse() {
        List<ReceiptLineItem> receiptLineItems = new ArrayList<>();

        ArrayList<String> order_content = GetOrderContent();
        resolveInlined(order_content);
        ArrayList<Integer> categoryPositions = FindEachCategoryPosition(order_content);

        //Resolve the item sections for each category
        // startLine, endLine: start and end of each category
        for (int categoryIndex = 0; categoryIndex < categoryPositions.size() - 1; categoryIndex++) {
            int startLine = categoryPositions.get(categoryIndex);
            int endLine = categoryPositions.get(categoryIndex + 1);
            if ((endLine - startLine - 2) % ColesReceiptItem.EXPECTED_ITEM__SECTION_LINE != 0) {
                ResolveTheItemSection(order_content, startLine + 1, endLine - 1);
            }
        }

        RemoveCategoryNameChunk(order_content);

        // Add to receiptLineItems for each item section
        for (int index = 0; index < order_content.size(); index = index + ColesReceiptItem.EXPECTED_ITEM__SECTION_LINE)
            receiptLineItems.add(ParseItem(order_content.subList(index, index + ColesReceiptItem.EXPECTED_ITEM__SECTION_LINE)));

        return receiptLineItems;
    }

    private ReceiptLineItem ParseItem(List<String> itemSection) {
        ReceiptLineItem receiptLineItem = new ReceiptLineItem();
        for (int i = 1; i < ColesReceiptItem.EXPECTED_ITEM__SECTION_LINE - 1; i++) {
            String line = itemSection.get(i);
            if (ColesReceiptItem.Is_Price(line))
                receiptLineItem.price = Float.parseFloat(line.substring(1));
            else if (ColesReceiptItem.Is_Quantity(line))
                receiptLineItem.quantity = Integer.parseInt(line);
            else
                receiptLineItem.itemDescription = line;
        }
        receiptLineItem.unitPrice = receiptLineItem.price / receiptLineItem.quantity;

        return receiptLineItem;
    }

    //Remove empty line or 'Quantity Price" line
    private void RemoveCategoryNameChunk(ArrayList<String> order_content) {
        for (int index = order_content.size() - 1; index >= 0; index--) {
            if (ColesReceiptItem.Is_Empty(order_content.get(index))) order_content.remove(index);
            else if (ColesReceiptItem.Is_Quantity_Price_Keyword(order_content.get(index))) {
                order_content.remove(index + 1);
                order_content.remove(index);
            }
        }
    }

    // Re-connect the description name of each item from multiple line to one line
    private void ResolveTheItemSection(ArrayList<String> order_content, int startLine, int endLine) {
        int currentLine = endLine;
        while (currentLine > startLine) {
            // End of each item section must be the line with " price PER unit"
            if (!ColesReceiptItem.Is_Per_Unit_Price_Line(order_content.get(currentLine))) {
                StringBuilder temp = new StringBuilder();
                while (!ColesReceiptItem.Is_Per_Unit_Price_Line(order_content.get(currentLine))) {
                    temp.insert(0, order_content.get(currentLine) + " ");
                    order_content.set(currentLine, "");
                    currentLine--;
                }

                // Identify the first line of description
                int lineGapBetweenDescriptionParts = 0;
                for (int i = 1; i < ColesReceiptItem.EXPECTED_ITEM__SECTION_LINE - 1; i++) {
                    String line = order_content.get(currentLine - i);
                    if (!ColesReceiptItem.Is_Price(line) && !ColesReceiptItem.Is_Quantity(line)) {
                        lineGapBetweenDescriptionParts = i;
                        break;
                    }
                }
                // Assign and concat the full description for item
                String fullItemDescription = order_content.get(currentLine - lineGapBetweenDescriptionParts) + " " + temp;
                order_content.set(currentLine - lineGapBetweenDescriptionParts, fullItemDescription.trim());
            }

            currentLine -= ColesReceiptItem.EXPECTED_ITEM__SECTION_LINE;
        }

    }

    private boolean IsReceiptHeader(String line) {
        return line.equals(receipt_Header);
    }

    private boolean IsReceiptFooter(String line) {
        return line.startsWith(receipt_Footer);
    }
}
