package com.deepmodi.shareden.model;

import java.util.List;

public class MyPostView {

    private String postId;
    private String userFullName;
    private String userLevel;
    private String userImg;
    private String userBookDescription;
    private String postTime;
    private List<String> userUploadBookList;
    private String userBookName;
    private String userBookAuthor;

    public MyPostView() {
    }

    public MyPostView(String postId, String userFullName, String userLevel, String userImg, String userBookDescription, String postTime, List<String> userUploadBookList, String userBookName, String userBookAuthor) {
        this.postId = postId;
        this.userFullName = userFullName;
        this.userLevel = userLevel;
        this.userImg = userImg;
        this.userBookDescription = userBookDescription;
        this.postTime = postTime;
        this.userUploadBookList = userUploadBookList;
        this.userBookName = userBookName;
        this.userBookAuthor = userBookAuthor;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
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
}
