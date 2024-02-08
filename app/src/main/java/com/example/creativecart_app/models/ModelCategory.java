package com.example.creativecart_app.models;

public class ModelCategory {

    //variables
    String category;
    int icon;

    //constructor with all param
    public ModelCategory(String category, int icon) {
        this.category = category;
        this.icon = icon;
    }

    //Getter and Setters to get and set item to/from ModelCategory of list
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
