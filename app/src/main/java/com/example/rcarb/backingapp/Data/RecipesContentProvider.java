package com.example.rcarb.backingapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by rcarb on 12/15/2017.
 */

public class RecipesContentProvider extends ContentProvider {

    //Integer constants for each directory of table and single item of the table.
    //Recipe table integer constants.
    public static final int RECIPES = 100;
    public static final int RECIPE_WITH_ID = 101;

    //Integer constants for ingredients table.
    public static final int INGREDIENTS = 200;
    public static final int INGREDIENTS_WITH_FOREIGN_KEY= 201;

    //Integer constants for steps.
    public static final int STEPS =300;
    public static final int STEPS_WITH_FOREIGN_KEY =301;

    //UirMatcher as a global variable.
    public static UriMatcher sUriMatcher = buildUriMatcher();

    //Helper UriMatcher helper function.
    public static UriMatcher buildUriMatcher(){
        //Empty matcher stated by the NO_MATCH
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //Add the Uri that the matcher will recognize.

        //matcher for the table Recipes directory
        uriMatcher.addURI(BackingContract.AUTHORITY, BackingContract.PATH_RECIPE,RECIPES);
        //matcher for the table Recipes item.
        uriMatcher.addURI(BackingContract.AUTHORITY, BackingContract.PATH_RECIPE  +"/#", RECIPE_WITH_ID);

        //Ingredients table matcher Uri.
        uriMatcher.addURI(BackingContract.AUTHORITY, BackingContract.PATH_INGREDINETS, INGREDIENTS);
        uriMatcher.addURI(BackingContract.AUTHORITY, BackingContract.PATH_INGREDINETS +"/#", INGREDIENTS_WITH_FOREIGN_KEY);

        //Steps table matcher Uri.
        uriMatcher.addURI(BackingContract.AUTHORITY, BackingContract.PATH_STEPS, STEPS);
        uriMatcher.addURI(BackingContract.AUTHORITY, BackingContract.PATH_STEPS +"/#", STEPS_WITH_FOREIGN_KEY);
        return uriMatcher;
    }

    //DBhelper class declaration.
    private SQLDatabaseHelper mDbHeleper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDbHeleper = new SQLDatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        //Get readable database
        final SQLiteDatabase db = mDbHeleper.getReadableDatabase();
        //Get the matched uri from the matcher.
        int match = sUriMatcher.match(uri);

        Cursor cursor;

        //Define the switch statement
        switch (match){
            //Query the Recipe directory.
            case RECIPES:
                cursor = db.query(BackingContract.RecipeEntry.RECIPES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        BackingContract.RecipeEntry.RECIPES_ID);

                break;
            case INGREDIENTS:
                cursor = db.query(BackingContract.RecipeEntry.INGREDIENTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case STEPS:
                cursor = db.query(BackingContract.RecipeEntry.STEPS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        BackingContract.RecipeEntry.STEPS_ID);
                break;
                //Throw exeption
            default:
                throw new UnsupportedOperationException("Unknown ur: " + uri);
        }
        //Tell the cursor what content uri it was created for.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri,
                      @Nullable ContentValues values) {
        //Declare the DbHelper to write to the database.
        final SQLiteDatabase db = mDbHeleper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        //variable that will store the returned Uri of the switch statement.
        Uri returnedUri;
        ContentValues contentValues = new ContentValues(values);

        switch (match){
            case RECIPES:
                 long id = db.insert(BackingContract.RecipeEntry.RECIPES_TABLE_NAME, null,contentValues);

                //if statement that checks that the insert was successful.
                if (id>0){
                    //Success
                    returnedUri = ContentUris.withAppendedId(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES, id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into "+ uri);
                }
                break;

            case INGREDIENTS:
                long ingredientId = db.insert(BackingContract.RecipeEntry.INGREDIENTS_TABLE_NAME, null, contentValues);
                //If successful
                if (ingredientId>0){
                    //Success
                    returnedUri = ContentUris.withAppendedId(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES_INGREDIENTS, ingredientId);
                }else {
                    throw new android.database.SQLException("Failed to insert new row into "+ uri);
                }
                break;

            case STEPS:
                long stepsId = db.insert(BackingContract.RecipeEntry.STEPS_TABLE_NAME, null, contentValues);
                if (stepsId > 0){
                    returnedUri = ContentUris.withAppendedId(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES_STEPS, stepsId);
                }else{
                    throw new android.database.SQLException("Failed to insert new row into "+ stepsId);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        //Notify the resolver that a change has occurd in the particular uri.
        getContext().getContentResolver().notifyChange(uri,null);

        return returnedUri;
    }

    @Override
    public int delete(@NonNull Uri uri,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri,
                      @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {

        return 0;
    }
}
