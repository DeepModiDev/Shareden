package com.deepmodi.shareden.model;

public class UserRegisterClass {
   private String userName;
   private String userNumber;
   private String userPassword;
   private String userGender;
   private String userLevel;
   private String userId;
   private String userImg;
   private String userDesc;
   private String userFirstTime;

    public UserRegisterClass() {
    }

    public UserRegisterClass(String userName, String userNumber, String userPassword, String userGender, String userLevel, String userId,String userFirstTime) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.userPassword = userPassword;
        this.userGender = userGender;
        this.userLevel = userLevel;
        this.userId = userId;
        this.userFirstTime = userFirstTime;
    }

    public UserRegisterClass(String userName, String userNumber, String userPassword, String userGender, String userLevel, String userId, String userImg,String userFirstTime) {
        this.userName = userName;
        this.userNumber = userNumber;
        this.userPassword = userPassword;
        this.userGender = userGender;
        this.userLevel = userLevel;
        this.userId = userId;
        this.userImg = userImg;
        this.userFirstTime = userFirstTime;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserFirstTime() {
        return userFirstTime;
    }

    public void setUserFirstTime(String userFirstTime) {
        this.userFirstTime = userFirstTime;
    }
}
