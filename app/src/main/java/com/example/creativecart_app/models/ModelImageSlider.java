package com.example.creativecart_app.models;

public class ModelImageSlider {

    //Variable spelling and case should be same as in Firebase DB
    String id;
    String imageUrl;

    //Empty constructor required for Firebase DB
    public ModelImageSlider() {
    }

    //Constructor with all parameter
    public ModelImageSlider(String id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    //Getters And Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
