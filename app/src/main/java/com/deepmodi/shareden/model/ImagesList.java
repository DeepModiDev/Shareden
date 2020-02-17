package com.deepmodi.shareden.model;

import java.util.List;

public class ImagesList {
    private List<String> imagesList;

    public ImagesList() {
    }

    public ImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }
}
