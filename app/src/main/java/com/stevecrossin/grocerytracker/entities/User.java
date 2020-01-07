package com.stevecrossin.grocerytracker.entities;

import android.widget.EditText;

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

    @ColumnInfo(name = "pass_key")
    private String passKey;

    @ColumnInfo(name = "login_status")
    private boolean isLoggedIn;

    @ColumnInfo(name = "user_gender")
    private String userGender;

    @ColumnInfo(name = "age")
    private int userAge;

    @ColumnInfo(name = "postcode")
    private int postCode;

    @ColumnInfo(name = "householdAdults")
    private String adultsInHouse;

    @ColumnInfo(name = "householdChildren")
    private String childrenInHouse;

    public User(EditText etName, EditText etAge, EditText etHeight, EditText etWeight, EditText etGender, EditText etPassword, EditText etNumberOfHouseHoldMember, EditText etHouseHoldMkeup, EditText etEmail, EditText etPassword1) {
    }

    /**
     * Getter and setter methods for the database. Each method returns or sets the relevant field in the database
     * Some setter methods are not utilised as the fields are never called to be changed (e.g. id, category) as they are fixed values.
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

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getAdultsInHouse() {
        return adultsInHouse;
    }

    public void setAdultsInHouse(String adultsInHouse) {
        this.adultsInHouse = adultsInHouse;
    }

    public String getChildrenInHouse() {
        return childrenInHouse;
    }

    public void setChildrenInHouse(String childrenInHouse) {
        this.childrenInHouse = childrenInHouse;
    }

    /** Constructor **/

    public User(int userID, String userName, String passKey, boolean isLoggedIn, String userGender, int userAge, int postCode, String adultsInHouse, String childrenInHouse) {
        this.userID = userID;
        this.userName = userName;
        this.passKey = passKey;
        this.isLoggedIn = isLoggedIn;
        this.userGender = userGender;
        this.userAge = userAge;
        this.postCode = postCode;
        this.adultsInHouse = adultsInHouse;
        this.childrenInHouse = childrenInHouse;


    }
}


