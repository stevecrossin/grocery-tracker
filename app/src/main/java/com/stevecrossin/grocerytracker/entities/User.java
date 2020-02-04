package com.stevecrossin.grocerytracker.entities;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//** Users table structure in room database//
@Entity(tableName = "user", indices = {@Index(value = {"email"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userID;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "pass_key")
    private String passKey;

    @ColumnInfo(name = "age")
    private String userAge;

    @ColumnInfo(name = "height")
    private String userHeight;

    @ColumnInfo(name = "weight")
    private String userWeight;

    @ColumnInfo(name = "user_gender")
    private String userGender;

    @ColumnInfo(name = "postcode")
    private String postCode;

    @ColumnInfo(name = "householdMembers")
    private String householdMembers;

    @ColumnInfo(name = "householdAdults")
    private String houseHoldAdults;

    @ColumnInfo(name = "householdChildren")
    private String houseHoldChildren;

    @ColumnInfo(name ="login_status")
    private boolean isLoggedIn;

    @ColumnInfo(name = "shop_number")
    private String shopNumber;

    /**
     * Getter & Setters
     */
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    public String getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(String userWeight) {
        this.userWeight = userWeight;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getHouseholdMembers() {
        return householdMembers;
    }

    public void setHouseholdMembers(String householdMembers) {
        this.householdMembers = householdMembers;
    }

    public String getHouseHoldAdults() {
        return houseHoldAdults;
    }

    public void setHouseHoldAdults(String houseHoldAdults) {
        this.houseHoldAdults = houseHoldAdults;
    }

    public String getHouseHoldChildren() {
        return houseHoldChildren;
    }

    public void setHouseHoldChildren(String houseHoldChildren) {
        this.houseHoldChildren = houseHoldChildren;
    }

    public String getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(String shopNumber) {
        this.shopNumber = shopNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassKey() {
        return passKey;
    }

    public void setPassKey(String passKey) {
        this.passKey = passKey;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public User(String userName, String email, String passKey, String userAge, String userHeight, String userWeight, String userGender, String postCode, String householdMembers, String houseHoldAdults, String houseHoldChildren, String shopNumber) {
        this.userName = userName;
        this.email = email;
        this.passKey = passKey;
        this.userAge = userAge;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.userGender = userGender;
        this.postCode = postCode;
        this.householdMembers = householdMembers;
        this.houseHoldAdults = houseHoldAdults;
        this.houseHoldChildren = houseHoldChildren;
        this.shopNumber = shopNumber;
    }


}


