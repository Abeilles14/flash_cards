package com.isabelle.flash.models;

public abstract class CardItem {
    private String title;
    private long id;        //use to find in SQLite DB
    private int color;      //to separate by colour?

    protected CardItem() {
    }

    protected CardItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
