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

    /**
     * Assumption: Each item has EXPECTED_ITEM_SECTION_LINE information
     *
     * Parse the Coles receipt
     *
     * The parse concept follow those steps:
     * Remove all unnecessary info of the receipt, only keep the purchased items info
     * Parse all inline information into separate lines (Ex: if price and quantity are in the same line then they need to be separated
     * As Coles separate items into different category so we need to identify the start and end of each category
     * Check for each category whether its items has follow the assumption about number of line. If not, it means
     *  there are items which the name stay in 2 or more line -> Resolve the name to put it all in one line
     *
     * After those steps, each item should align with the rules EXPECTED_ITEM_SECTION_LINE (=5)
     *  includes: branch, price, name ,unit price, quantity and is ready to parse to the ReceiptLineItem list
     *
     * @return the ReceiptLineItem list of object
     */
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


    /**
     * Get the header and footer default of the email text format
     */
    private void IdentifyHeaderAndFooter() {
        receipt_Header = receipt_lines.length > 0 ? receipt_lines[0] : null;
        receipt_Footer = receipt_lines.length > 1 ? receipt_lines[receipt_lines.length - 1].split(" ")[0] : null;
    }

    /**
     * Remove all unnecessary parts of the receipts, includes:
     *  Email Footer and Header, '...' symbols, blank line
     * Get the main content of the receipt information
     * @return The list of purchased items of receipt information
     */
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

    /**
     * Fnd and store the start and end position of each category
     * @param order_content: the receipt content
     * @return a list of position represent for the position of the category
     */
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


    /**
     * When we have line with quantity and price -> separate into 2 separate lines
     * Steps: if any line appears the $ symbol and is not the Unit Price only or price only
     *  -> the line contains 2 piece of info (price and unit price)
     * @param order_content the receipt content
     */
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


    /**
     * Parse each line based on its characteristic to be a price , quantity or description
     * @param itemSection the EXPECTED_ITEM__SECTION_LINE (=5) information of the item
     * @return the ReceiptLineItem object of the purchased item
     */
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


    /**
     * Remove chunk of empty line or label lines
     * @param order_content receipt content
     */
    private void RemoveCategoryNameChunk(ArrayList<String> order_content) {
        for (int index = order_content.size() - 1; index >= 0; index--) {
            if (ColesReceiptItem.Is_Empty(order_content.get(index))) order_content.remove(index);
            else if (ColesReceiptItem.Is_Quantity_Price_Keyword(order_content.get(index))) {
                order_content.remove(index + 1);
                order_content.remove(index);
            }
        }
    }

    /**
     * If the description of the item is in different lines so we need to concat all the piece of info into one line
     * @param order_content : the receipt content
     * @param startLine : the start index of the line included info of the item
     * @param endLine : the end index of the line included info of the item
     */
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
