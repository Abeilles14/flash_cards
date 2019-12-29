package com.isabelle.flash.models;

import java.util.ArrayList;

public class Category extends CardItem{


    //TODELETE
    private String categoryName;

    private ArrayList<Deck> decks;      //list of decks of this category
    private String description;

    public Category(){
    }

    //TODELETE
    //use to populate categories array for testing
    public Category(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    public Category(String categoryName, ArrayList<Deck> decks) {
        super(categoryName);
        this.decks = decks;
        this.description = "Description";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
