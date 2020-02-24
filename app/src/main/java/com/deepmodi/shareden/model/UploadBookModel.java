package com.deepmodi.shareden.model;

import java.util.List;

public class UploadBookModel {

    private UserRegisterClass register;
    private List<String> imagesList;

    public UploadBookModel() {
    }

    public UploadBookModel(UserRegisterClass register, List<String> imagesList) {
        this.register = register;
        this.imagesList = imagesList;
    }

    public UserRegisterClass getRegister() {
        return register;
    }

    public void setRegister(UserRegisterClass register) {
        this.register = register;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }
}
