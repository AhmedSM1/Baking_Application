package com.ahmed.bakingapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmed.bakingapplication.RecipeContract;

public class RecipeHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Recipe.db";
    private static final int DATABASE_VERSION = 1;


    public RecipeHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_QUERY = "CREATE TABLE " +
                RecipeContract.RecipeEntry.TABLE_NAME + " ( " +
                RecipeContract.RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeContract.RecipeEntry.RECIPE_TITLE_COLUMN + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.INGREDIENTS_COLUMN + " TEXT NOT NULL, " +
                RecipeContract.RecipeEntry.TIME_STAMP_COLUMN + " TIMESTAMP NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }

}
