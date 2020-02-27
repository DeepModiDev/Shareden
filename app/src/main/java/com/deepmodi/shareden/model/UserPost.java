package com.deepmodi.shareden.model;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class UserPost {

    private String userFullName;
    private String userLevel;
    private String userImg;
    private String userBookDescription;
    private String userFollowStatus;
    private String postId;
    private String postTime;
    private List<String> userUploadBookList;
    private String userBookName;
    private String userBookAuthor;
    private String userPhoneNumber;
    private String selectedBookType;


    public UserPost() {
    }

    public UserPost(String userFullName, String userLevel, String userImg, String userBookDescription, String userFollowStatus) {
        this.userFullName = userFullName;
        this.userLevel = userLevel;
        this.userImg = userImg;
        this.userBookDescription = userBookDescription;
        this.userFollowStatus = userFollowStatus;
    }

    public UserPost(String userFullName, String userLevel, String userImg, String userBookDescription, String userFollowStatus, List<String> userUploadBookList) {
        this.userFullName = userFullName;
        this.userLevel = userLevel;
        this.userImg = userImg;
        this.userBookDescription = userBookDescription;
        this.userFollowStatus = userFollowStatus;
        this.userUploadBookList = userUploadBookList;
    }

    public UserPost(String userFullName, String userLevel, String userImg, String userBookDescription, String userFollowStatus, String postId, String postTime, List<String> userUploadBookList) {
        this.userFullName = userFullName;
        this.userLevel = userLevel;
        this.userImg = userImg;
        this.userBookDescription = userBookDescription;
        this.userFollowStatus = userFollowStatus;
        this.postId = postId;
        this.postTime = postTime;
        this.userUploadBookList = userUploadBookList;
    }

    public UserPost(String userFullName, String userLevel, String userImg, String userBookDescription, String userFollowStatus, String postId, String postTime, List<String> userUploadBookList, String userBookName, String userBookAuthor) {
        this.userFullName = userFullName;
        this.userLevel = userLevel;
        this.userImg = userImg;
        this.userBookDescription = userBookDescription;
        this.userFollowStatus = userFollowStatus;
        this.postId = postId;
        this.postTime = postTime;
        this.userUploadBookList = userUploadBookList;
        this.userBookName = userBookName;
        this.userBookAuthor = userBookAuthor;
    }

    public UserPost(String userFullName, String userLevel, String userImg, String userBookDescription, String userFollowStatus, String postId, String postTime, List<String> userUploadBookList, String userBookName, String userBookAuthor, String userPhoneNumber) {
        this.userFullName = userFullName;
        this.userLevel = userLevel;
        this.userImg = userImg;
        this.userBookDescription = userBookDescription;
        this.userFollowStatus = userFollowStatus;
        this.postId = postId;
        this.postTime = postTime;
        this.userUploadBookList = userUploadBookList;
        this.userBookName = userBookName;
        this.userBookAuthor = userBookAuthor;
        this.userPhoneNumber = userPhoneNumber;
    }

    public UserPost(String userFullName, String userLevel, String userImg, String userBookDescription, String userFollowStatus, String postId, String postTime, List<String> userUploadBookList, String userBookName, String userBookAuthor, String userPhoneNumber, String selectedBookType) {
        this.userFullName = userFullName;
        this.userLevel = userLevel;
        this.userImg = userImg;
        this.userBookDescription = userBookDescription;
        this.userFollowStatus = userFollowStatus;
        this.postId = postId;
        this.postTime = postTime;
        this.userUploadBookList = userUploadBookList;
        this.userBookName = userBookName;
        this.userBookAuthor = userBookAuthor;
        this.userPhoneNumber = userPhoneNumber;
        this.selectedBookType = selectedBookType;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserBookName() {
        return userBookName;
    }

    public void setUserBookName(String userBookName) {
        this.userBookName = userBookName;
    }

    public String getUserBookAuthor() {
        return userBookAuthor;
    }

    public void setUserBookAuthor(String userBookAuthor) {
        this.userBookAuthor = userBookAuthor;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public List<String> getUserUploadBookList() {
        return userUploadBookList;
    }

    public void setUserUploadBookList(List<String> userUploadBookList) {
        this.userUploadBookList = userUploadBookList;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }


    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName)
    {
        this.userFullName = userFullName;
    }

    public String getUserImg() {
        return userImg;
    }
    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserBookDescription() {
        return userBookDescription;
    }

    public void setUserBookDescription(String userBookDescription) {
        this.userBookDescription = userBookDescription;
    }

    public String getUserFollowStatus() {
        return userFollowStatus;
    }

    public void setUserFollowStatus(String userFollowStatus) {
        this.userFollowStatus = userFollowStatus;
    }

    public String getSelectedBookType() {
        return selectedBookType;
    }

    public void setSelectedBookType(String selectedBookType) {
        this.selectedBookType = selectedBookType;
    }
}
