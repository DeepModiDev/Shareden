package com.deepmodi.shareden.model;

import java.util.List;

public class UploadBookModel {

    private UserRegister register;
    private List<String> imagesList;

    public UploadBookModel() {
    }

    public UploadBookModel(UserRegister register, List<String> imagesList) {
        this.register = register;
        this.imagesList = imagesList;
    }

    public UserRegister getRegister() {
        return register;
    }

    public void setRegister(UserRegister register) {
        this.register = register;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }
}
