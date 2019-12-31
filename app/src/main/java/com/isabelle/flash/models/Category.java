package com.isabelle.flash.models;

import java.util.ArrayList;

public class Category extends CardItem{


    //TODELETE
    private String categoryName;

    private ArrayList<Deck> decks;      //list of decks of this category

    public Category(){
    }

    //TODELETE
    //use to populate categories array for testing
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(String categoryName, ArrayList<Deck> decks) {
        super(categoryName);
        this.decks = decks;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
