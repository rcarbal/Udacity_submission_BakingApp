package com.example.rcarb.backingapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rcarb on 12/14/2017.
 */

public class SQLDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABSE_NAME ="BakingAppRecipes.db";
    private static final int DATABSE_VERION = 1;
    public SQLDatabaseHelper(Context context) {
        super(context, DATABSE_NAME, null, DATABSE_VERION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BackingContract.RecipeEntry.SQL_CREATE_RECIPES_ENTRIES);
        db.execSQL(BackingContract.RecipeEntry.SQL_CREATE_INGREDIENTS_ENTRIES);
        db.execSQL(BackingContract.RecipeEntry.SQL_CREATE_STEPS_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(BackingContract.RecipeEntry.SQL_DELETE_RECIPE_ENTRIES);
        db.execSQL(BackingContract.RecipeEntry.SQL_DELETE_INGREDIENTS_ENTRIES);
        db.execSQL(BackingContract.RecipeEntry.SQL_DELETE_STEPS_ENTRIES);

        onCreate(db);
    }
}
