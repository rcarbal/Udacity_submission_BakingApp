package com.example.rcarb.backingapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class BackingContract {

    //Uri that will be used by the URI Matcher.
    public static final String AUTHORITY = "com.example.rcarb.backingapp";

    //Base content uri that will be the start for accessing our provider.
    @SuppressWarnings("WeakerAccess")
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //String for accessing the different tables.
    public final static String PATH_RECIPE = "Recipes";
    public final static String PATH_INGREDINETS = "Recipes Ingredients";
    public final static String PATH_STEPS = "Recipes Steps";


    public static class RecipeEntry implements BaseColumns {
        //Uri that point to the three tables the Recipes, Recipes Ingredients, Recipe Steps.
        public static final Uri BASE_CONTENT_URI_RECIPES =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();
        public static final Uri BASE_CONTENT_URI_RECIPES_INGREDIENTS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDINETS).build();
        public static final Uri BASE_CONTENT_URI_RECIPES_STEPS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();
       //Constant to be used in the widget.
        public static final int INVALID_RECIPE = -1;

        //Table recipe parent constants
        public static final String RECIPES_TABLE_NAME = "Recipes";
        public static final String RECIPES_ID = "Recipe_Id";
        public final static String RECIPES_NAME = "Recipe_Name";
        public final static String RECIPES_SERVINGS = "Recipe_Servings";
        public final static String RECIPES_IMAGE = "Recipe_Image";

        //Table ingredients child constants
        public final static String INGREDIENTS_TABLE_NAME = "Ingredients";
        public final static String INGREDIENTS_QUANTITY = "Quantity";
        public final static String INGREDIENTS_MEASURE = "Measure";
        public final static String INGREDIENT_INGREDIENTS = "Ingredients";
        public final static String INGREDINETS_RECIPE_ID = "Recipe_Id";

        //Table steps child constants
        public final static String STEPS_TABLE_NAME = "Steps";
        public final static String STEPS_ID = "Steps_Id";
        public final static String STEPS_SHORT_DESCRIPTION = "Short_Description";
        public final static String STEPS_DESCRIPTION = "Description";
        public final static String STEPS_VIDEO_URL = "Video_URL";
        public final static String STEPS_THUMBNAIL_URL = "Thumbnail_URL";
        public final static String STEPS_RECIPE_ID = "Recipe_id";

        //Create SQL tables Strings.
        //String for Recipe table
        public final static String SQL_CREATE_RECIPES_ENTRIES =
                "CREATE TABLE " + RecipeEntry.RECIPES_TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                        RecipeEntry.RECIPES_ID + " INTEGER NOT NULL, " +
                        RecipeEntry.RECIPES_NAME + " TEXT NOT NULL, " +
                        RecipeEntry.RECIPES_SERVINGS + " INTEGER NOT NULL, " +
                        RecipeEntry.RECIPES_IMAGE + " TEXT)";

        //String for ingredient table.
        public final static String SQL_CREATE_INGREDIENTS_ENTRIES =
                "CREATE TABLE " + RecipeEntry.INGREDIENTS_TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                        RecipeEntry.INGREDIENTS_QUANTITY + " REAL NOT NULL, " +
                        RecipeEntry.INGREDIENTS_MEASURE + " TEXT NOT NULL, " +
                        RecipeEntry.INGREDIENT_INGREDIENTS + " TEXT NOT NULL, " +
                        RecipeEntry.INGREDINETS_RECIPE_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" +
                        RecipeEntry.INGREDINETS_RECIPE_ID + ") REFERENCES " +
                        RecipeEntry.RECIPES_TABLE_NAME + "(" + RECIPES_ID + "))";

        //String for steps String.
        public final static String SQL_CREATE_STEPS_ENTRIES =
                "CREATE TABLE " + RecipeEntry.STEPS_TABLE_NAME + " (" +
                        RecipeEntry._ID + " INTEGER PRIMARY KEY, " +
                        RecipeEntry.STEPS_ID + " INTEGER NOT NULL, " +
                        RecipeEntry.STEPS_SHORT_DESCRIPTION + " TEXT NOT NULL, " +
                        RecipeEntry.STEPS_DESCRIPTION + " TEXT NOT NULL, " +
                        RecipeEntry.STEPS_VIDEO_URL + " TEXT, " +
                        RecipeEntry.STEPS_THUMBNAIL_URL + " TEXT, " +
                        RecipeEntry.STEPS_RECIPE_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" +
                        RecipeEntry.STEPS_RECIPE_ID + ") REFERENCES " +
                        RecipeEntry.RECIPES_TABLE_NAME + "(" + RECIPES_ID + "))";

        //Deletes the recipe databse
        public static final String SQL_DELETE_RECIPE_ENTRIES =
                "DROP TABLE IF EXISTS " + RecipeEntry.RECIPES_TABLE_NAME;

        //Deletes the ingredient databse
        public static final String SQL_DELETE_INGREDIENTS_ENTRIES =
                "DROP TABLE IF EXISTS " + RecipeEntry.INGREDIENTS_TABLE_NAME;

        //Deletes the steps databse
        public static final String SQL_DELETE_STEPS_ENTRIES =
                "DROP TABLE IF EXISTS " + RecipeEntry.STEPS_TABLE_NAME;


    }
}
