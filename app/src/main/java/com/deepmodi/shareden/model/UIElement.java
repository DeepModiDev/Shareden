package com.deepmodi.shareden.model;

import java.util.HashMap;

public class UIElement {

    private String text;
    private boolean isSelected = false;

    public UIElement() {
    }

    public UIElement(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }
}
