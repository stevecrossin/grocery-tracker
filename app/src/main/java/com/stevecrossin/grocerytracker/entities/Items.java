package com.stevecrossin.grocerytracker.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity
public class Items {
    //** Users table structure in room database//

        @PrimaryKey(autoGenerate = true)
        private int itemID;

        @ColumnInfo(name = "item_name")
        private String itemName;

        @ColumnInfo(name = "item_category")
        private String itemCategory;

        /**
         * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
         * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
         */

        public int getItemID() {
            return itemID;
        }

        public void setItemID(int itemID) {
            this.itemID = itemID;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemCategory() {
            return itemCategory;
        }

        public void setItemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        /**Constructor
        //public User(int itemID, String itemName, String itemCategory) {
            this.itemID = itemID;
            this.itemName = itemName;
            this.itemCategory = itemCategory;
        //}
         **/
    }


