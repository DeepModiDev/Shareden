package com.deepmodi.shareden.model;

public class User  {

    private String UserName;
    private String UserLevel;
    private String UserNumber;
    private String UserDescription;
    private String UserImageUrl;


    public User() {
    }

    public User(String userName, String userLevel, String userNumber, String userDescription, String userImageUrl) {
        UserName = userName;
        UserLevel = userLevel;
        UserNumber = userNumber;
        UserDescription = userDescription;
        UserImageUrl = userImageUrl;
    }

    public String getUserName() {
        return UserName;
    }


    public void setUserName(String userName) {
        UserName = userName;

    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }

    public String getUserNumber() {
        return UserNumber;
    }

    public void setUserNumber(String userNumber) {
        UserNumber = userNumber;
    }

    public String getUserDescription() {
        return UserDescription;
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
    }

    public String getUserImageUrl() {
        return UserImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        UserImageUrl = userImageUrl;
    }

}
