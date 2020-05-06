package com.stevecrossin.grocerytracker.models;

public class ReceiptLineItem {
    public String itemDescription;
    public float unitPrice;
    public float quantity;
    public float price;

    public ReceiptLineItem() {
    }

    public ReceiptLineItem(String line) {
        String[] columns = line.split(",");
        itemDescription = (columns.length > 0) ? columns[0] : "";
        unitPrice = (columns.length > 1) ? Float.parseFloat(columns[1]) : 0.0f;
        quantity = (columns.length > 2) ? Float.parseFloat(columns[2]) : 0.0f;
        price = (columns.length > 3) ? Float.parseFloat(columns[3]) : 0.0f;
    }

    public static class Header {
        public String itemDescriptionHeader;
        public String unitPriceHeader;
        public String quantityHeader;
        public String priceHeader;

        public Header(String line) {
            String[] columnHeaders = line.split(",");
            itemDescriptionHeader = (columnHeaders.length > 0) ? columnHeaders[0] : "";
            unitPriceHeader = (columnHeaders.length > 1) ? columnHeaders[1] : "";
            quantityHeader = (columnHeaders.length > 2) ? columnHeaders[2] : "";
            priceHeader = (columnHeaders.length > 3) ? columnHeaders[3] : "";
        }
    }
}