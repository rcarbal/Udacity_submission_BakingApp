package com.example.rcarb.backingapp.LoadersAndAsyncTasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.rcarb.backingapp.Data.BackingContract;
import com.example.rcarb.backingapp.Utilities.RecipeIngredientsSub;

import java.util.ArrayList;

public class GetIngredientsAsyncTaskLoader extends AsyncTaskLoader<ArrayList<RecipeIngredientsSub>> {

    private final int mRecipeId;

    public GetIngredientsAsyncTaskLoader(Context context, int recipeId) {
        super(context);

        mRecipeId = recipeId;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<RecipeIngredientsSub> loadInBackground() {
        @SuppressWarnings("UnusedAssignment")
        Cursor cursor = null;
        int recipeId = mRecipeId;

        @SuppressWarnings("UnusedAssignment")
        String mSelectionClause = null;
        String[] mSelectionArgs = new String[1];

        //If reciep_id is null;
        if (recipeId <0){
            return null;
        }else{
            mSelectionClause = BackingContract.RecipeEntry.INGREDINETS_RECIPE_ID + " =?";
            mSelectionArgs[0] = String.valueOf(recipeId);
        }

        //Try to parse the database.
        try {
            cursor = getContext().getContentResolver().query(
                    BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES_INGREDIENTS,
                    null,
                    mSelectionClause,
                    mSelectionArgs,
                    BackingContract.RecipeEntry._ID,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GetRecipeStepsLoader", "parse failed" + e);
            return null;
        }

        ArrayList<RecipeIngredientsSub>  fullIngredients = new ArrayList<>();

        assert cursor != null;
        if (cursor.moveToFirst()){
            do {
                RecipeIngredientsSub recipe = new RecipeIngredientsSub();
                recipe.setQuantityValue(cursor.getDouble(cursor.getColumnIndex(
                        BackingContract.RecipeEntry.INGREDIENTS_QUANTITY)));
                recipe.setMeasureValue(cursor.getString(cursor.getColumnIndex(
                        BackingContract.RecipeEntry.INGREDIENTS_MEASURE)));
                recipe.setIngredientsValue(cursor.getString(cursor.getColumnIndex(
                        BackingContract.RecipeEntry.INGREDIENT_INGREDIENTS)));
                fullIngredients.add(recipe);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return  fullIngredients;
    }
}
