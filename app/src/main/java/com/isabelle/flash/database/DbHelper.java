package com.isabelle.flash.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.isabelle.flash.models.Category;
import com.isabelle.flash.models.Deck;
import com.isabelle.flash.models.FlashCard;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    //DB CONSTS
    private static final String LOG_TAG = "DbHelper";
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
        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CATEGORIES_TABLE +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE +
                " TEXT NOT NULL " + ");";
        //create table DECKS
        //_ID primary key autoincrement
        //TITLE not null
        // BELONGS_TO not null, foreign key references categories (_ID)
        final String SQL_CREATE_DECKS_TABLE = "CREATE TABLE " + DECKS_TABLE +
                " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE +
                " TEXT NOT NULL, " + BELONGS_TO + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + BELONGS_TO + ") REFERENCES " + CATEGORIES_TABLE +
                " (" + _ID + ")" + ");";
        //create table FLASHCARDS
        //_ID primary key autoincrement
        //TITLE not null
        //FLASHCARD_CONTENT not null, FLASHCARD_ANSWER not null
        //BELONGS_TO not null, foreign key references categories (_ID)
        final String SQL_CREATE_FLASHCARDS_TABLE = "CREATE TABLE " + FLASHCARDS_TABLE +
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

    ////////// CRUD METHODS //////////

    ////////CREATE
    //category
    public long createCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();     //get writable db to insert data
        ContentValues values = new ContentValues();     //create new content values with key value pairs
        values.put(TITLE, category.getTitle());         //insert title for category

        System.out.println("category created");
        return db.insert(CATEGORIES_TABLE, null, values);
    }

    //deck
    public long createDeck(Deck deck, long categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, deck.getTitle());
        values.put(BELONGS_TO, categoryId);     //get id of categories for reference through foreign key
        return db.insert(DECKS_TABLE, null, values);
    }

    //flashcard
    public long createFlashCard(FlashCard flashCard, long deckId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, "");
        values.put(FLASHCARD_CONTENT, flashCard.getContent());
        values.put(FLASHCARD_ANSWER, flashCard.getAnswer());
        values.put(BELONGS_TO, deckId);
        return db.insert(FLASHCARDS_TABLE, null, values);
    }

    /////////READ
    //category
    public Category getCategoryByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE + " WHERE " +
                TITLE + " = '" + title + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.i(LOG_TAG, "query returned null");
            return null;
        }

        //create item with corresponding properties to return
        Category category = new Category();
        category.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        category.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        this.closeDB();
        cursor.close();
        return category;
    }

    public Category getCategoryByID(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE + " WHERE " +
                _ID + " = " + id + ";";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.i(LOG_TAG, "query returned null");
            return null;
        }

        //create item with corresponding properties to return
        Category category = new Category();
        category.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        category.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        this.closeDB();
        cursor.close();
        return category;
    }

    public ArrayList<Category> getAllCategories(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + CATEGORIES_TABLE;

        ArrayList<Category> categories = new ArrayList<Category>();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();       //may be wrong?

        if((cursor != null) && (cursor.getCount()>0)) {     //check for valid cursor
            do{
                Category category = new Category();
                category.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
                category.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));

                categories.add(category);
            } while (cursor.moveToNext());
        }

        this.closeDB();
        cursor.close();
        return categories;
    }

    //deck
    public Deck getDeckByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DECKS_TABLE + " WHERE " +
                TITLE + " = '" + title + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.i(LOG_TAG, "query returned null");
            return null;
        }

        //create item with corresponding properties to return
        Deck deck = new Deck();
        deck.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        deck.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));

        Category category = getCategoryByID(cursor.getLong(cursor.getColumnIndex(BELONGS_TO)));

        if(category != null){
            deck.setCategory(category);
        }

        this.closeDB();
        cursor.close();
        return deck;
    }

    public Deck getDeckByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DECKS_TABLE + " WHERE " +
                _ID + " = '" + id + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.i(LOG_TAG, "query returned null");
            return null;
        }

        //create item with corresponding properties to return
        Deck deck = new Deck();
        deck.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        deck.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));

        Category category = getCategoryByID(cursor.getLong(cursor.getColumnIndex(BELONGS_TO)));

        if(category != null){
            deck.setCategory(category);
        }

        this.closeDB();
        cursor.close();
        return deck;
    }

    public ArrayList<Deck> getAllDecksByCategoryId(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DECKS_TABLE + " WHERE " + BELONGS_TO +
                " = " + id + ";";

        ArrayList<Deck> decks = new ArrayList<Deck>();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            do{
                Deck deck = new Deck();
                deck.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
                deck.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));

                deck.setCategory(getCategoryByID(id));
                decks.add(deck);
            } while (cursor.moveToNext());
        }

        this.closeDB();
        cursor.close();
        return decks;
    }

    //flashcard
    public FlashCard getFlashCardByID(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + FLASHCARDS_TABLE + " WHERE " +
                _ID + " = '" + id + "';";

        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            cursor.moveToFirst();
        } else {
            Log.i(LOG_TAG, "query returned null");
            return null;
        }

        //create item with corresponding properties to return
        FlashCard flashCard = new FlashCard();
        flashCard.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
        flashCard.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
        flashCard.setContent(cursor.getString(cursor.getColumnIndex(FLASHCARD_CONTENT)));
        flashCard.setAnswer(cursor.getString(cursor.getColumnIndex(FLASHCARD_ANSWER)));

        Deck deck = getDeckByID(cursor.getLong(cursor.getColumnIndex(BELONGS_TO)));

        if(deck != null){
            flashCard.setDeck(deck);
        }

        this.closeDB();
        cursor.close();
        return flashCard;
    }

    public ArrayList<FlashCard> getAllFlashCardsByDeckId(Long id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + FLASHCARDS_TABLE + " WHERE " +
                BELONGS_TO + " = '" + id + "';";

        ArrayList<FlashCard> flashCards = new ArrayList<FlashCard>();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor != null) {
            do{
                FlashCard flashCard = new FlashCard();
                flashCard.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
                flashCard.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                flashCard.setContent(cursor.getString(cursor.getColumnIndex(FLASHCARD_CONTENT)));
                flashCard.setAnswer(cursor.getString(cursor.getColumnIndex(FLASHCARD_ANSWER)));

                flashCard.setDeck(getDeckByID(id));
                flashCards.add(flashCard);
            } while (cursor.moveToNext());
        }

        this.closeDB();
        cursor.close();
        return flashCards;
    }

    //TODO remove commented undos
    ////////UPDATE
    //category
    public long updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID, category.getId());
        values.put(TITLE, category.getTitle());
        return db.insert(CATEGORIES_TABLE, null, values);
    }

    //deck
    public long updateDeck(Deck deck) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID, deck.getId());
        values.put(TITLE, deck.getTitle());
        values.put(BELONGS_TO, deck.getCategory().getId());
        return db.insert(DECKS_TABLE, null, values);
    }

    //flashcard
    public long  updateFlashCard(FlashCard flashCard) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(_ID, flashCard.getId());
        values.put(TITLE, flashCard.getTitle());
        values.put(FLASHCARD_CONTENT, flashCard.getContent());
        values.put(FLASHCARD_ANSWER, flashCard.getAnswer());
        values.put(BELONGS_TO, flashCard.getDeck().getId());
        return db.insert(FLASHCARDS_TABLE,null, values);
    }

    ////////DELETE
    //delete item from db
    public void deleteItem(long id, String table){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] strId = new String[] {String.valueOf(id)};
        System.out.println("item deleted");
        db.delete(table, _ID + " = ?", strId);
        this.closeDB();;
    }

    //delete all items from table
    public void deleteAllItemsByTable(String table){
        SQLiteDatabase db  = this.getWritableDatabase();
        db.delete(table,null,null);
        this.closeDB();;
    }

    //close db connection
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

}
