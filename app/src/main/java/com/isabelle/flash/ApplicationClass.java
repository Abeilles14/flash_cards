package com.isabelle.flash;

import android.app.Application;

import com.isabelle.flash.models.Category;

import java.util.ArrayList;

public class ApplicationClass extends Application {
    public static ArrayList<Category> categories;

    @Override
    public void onCreate() {
        super.onCreate();

        categories = new ArrayList<Category>();
        categories.add(new Category("Category 1", "Category 1 Description"));
        categories.add(new Category("Category 2", "Category 2 Description"));
        categories.add(new Category("Category 3", "Category 3 Description"));

    }
}
