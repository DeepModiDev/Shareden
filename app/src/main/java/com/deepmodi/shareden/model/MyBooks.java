package com.deepmodi.shareden.model;

import java.util.List;

public class MyBooks {

    private String bookDesc;
    private List<String> bookImages;
    private String bookId;

    public MyBooks() {
    }

    public MyBooks(String bookDesc, List<String> bookImages, String bookId) {
        this.bookDesc = bookDesc;
        this.bookImages = bookImages;
        this.bookId = bookId;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public void setBookDesc(String bookDesc) {
        this.bookDesc = bookDesc;
    }

    public List<String> getBookImages() {
        return bookImages;
    }

    public void setBookImages(List<String> bookImages) {
        this.bookImages = bookImages;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}
