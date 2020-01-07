package com.stevecrossin.grocerytracker.entities;

import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

//** Users table structure in room database//
@Entity(tableName = "user", indices = {@Index(value = {"user_name"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private int userID;

    @ColumnInfo(name = "user_name")
    private String userName;

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
    private String adultsInHouse;

    @ColumnInfo(name = "householdMakeup")
    private String houseHoldMakeup;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "pass_key")
    private String passKey;

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

    public String getAdultsInHouse() {
        return adultsInHouse;
    }

    public void setAdultsInHouse(String adultsInHouse) {
        this.adultsInHouse = adultsInHouse;
    }

    public String getHouseHoldMakeup() {
        return houseHoldMakeup;
    }

    public void setHouseHoldMakeup(String houseHoldMakeup) {
        this.houseHoldMakeup = houseHoldMakeup;
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

    public User(String userName, String userAge, String userHeight, String userWeight, String userGender, String postCode, String adultsInHouse, String houseHoldMakeup, String email, String passKey) {
        this.userName = userName;
        this.userAge = userAge;
        this.userHeight = userHeight;
        this.userWeight = userWeight;
        this.userGender = userGender;
        this.postCode = postCode;
        this.adultsInHouse = adultsInHouse;
        this.houseHoldMakeup = houseHoldMakeup;
        this.email = email;
        this.passKey = passKey;
    }
}


