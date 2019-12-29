package com.isabelle.flash.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    //DB CONSTS
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "flashcards.db";

    //TABLES
    public static final String CATEGORIES_TABLE = "categories";
    public static final String DECKS_TABLE = "decks";
    public static final String FLASHCARDS_TABLE = "flashcards";

    //COLUMNS
    public static final String _ID = "id";
    public static final String TITLE = "title";
    public static final String BELONGS_TO = "belongs_to";    //foreign keys?
    public static final String FLASHCARD_CONTENT = "content";   //flashcard cols
    public static final String FLASHCARD_ANSWER = "answer";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create database, only if db dne
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table CATEGORIES
        //_ID primary key autoincrement
        // TITLE not null
        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE" + CATEGORIES_TABLE +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE +
                " TEXT NOT NULL " + ");";
        //create table DECKS
        //_ID primary key autoincrement
        //TITLE not null
        // BELONGS_TO not null, foreign key references categories (_ID)
        final String SQL_CREATE_DECKS_TABLE = "CREATE TABLE" + DECKS_TABLE +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE +
                " TEXT NOT NULL " + BELONGS_TO + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + BELONGS_TO + ") REFERENCES " + CATEGORIES_TABLE +
                " (" + _ID + ")" + ");";
        //create table FLASHCARDS
        //_ID primary key autoincrement
        //TITLE not null
        //FLASHCARD_CONTENT not null, FLASHCARD_ANSWER not null
        //BELONGS_TO not null, foreign key references categories (_ID)
        final String SQL_CREATE_FLASHCARDS_TABLE = "CREATE TABLE" + FLASHCARDS_TABLE +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE +
                " TEXT NOT NULL, " + FLASHCARD_CONTENT + " TEXT NOT NULL, " +
                FLASHCARD_ANSWER + " TEXT NOT NULL, " + BELONGS_TO +
                " INTEGER NOT NULL, " + "FOREIGN KEY (" + BELONGS_TO +
                ") REFERENCES " + CATEGORIES_TABLE + " (" + _ID + ")" + ");";

        //create the tables
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_DECKS_TABLE);
        db.execSQL(SQL_CREATE_FLASHCARDS_TABLE);
    }

    //update db, add new tables/cols to existing table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //delete current tables then call onCreate
        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DECKS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FLASHCARDS_TABLE);
        onCreate(db);
    }

    //close db connection
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    ////////// CRUD METHODS //////////

    //CREATE



}
