package com.isabelle.flash;

import android.app.Application;

import com.isabelle.flash.models.Category;

import java.util.ArrayList;


//TO DELETE
//remember to remove from manifest
//First and last app to run, implements text view in categories and description

public class ApplicationClass extends Application {
    public static ArrayList<Category> categories;

    @Override
    public void onCreate() {
        super.onCreate();

        //creating instances of categories, adding to array list
        categories = new ArrayList<Category>();
        for (int i = 1; i < 6; i++) {
            categories.add(new Category("Category " + i,"Category "+ i + " Description"));
        }
    }
}
