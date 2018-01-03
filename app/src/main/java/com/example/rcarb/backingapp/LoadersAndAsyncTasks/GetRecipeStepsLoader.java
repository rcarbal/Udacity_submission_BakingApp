package com.example.rcarb.backingapp.LoadersAndAsyncTasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.util.Log;

import com.example.rcarb.backingapp.Data.BackingContract;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcarb on 12/29/2017.
 */

public class GetRecipeStepsLoader extends AsyncTaskLoader<ArrayList<RecipeStepsSub>> {

    private Context mContext;
    private int mRecipeId;

    public GetRecipeStepsLoader(Context context,
                                int reciepeId) {
        super(context);
        mContext = context;
        mRecipeId = reciepeId;

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<RecipeStepsSub> loadInBackground() {

        Cursor cursor = null;
        int recipeId = mRecipeId;

        String mSelectionClause = null;
        String[] mSelectionArgs = new String[1];

        //If reciep_id is null;
        if (recipeId <0){
            return null;
        }else{
            mSelectionClause = BackingContract.RecipeEntry.STEPS_RECIPE_ID + " =?";
            mSelectionArgs[0] = String.valueOf(recipeId);
        }

        //Try to parse the database.
        try {
            cursor = getContext().getContentResolver().query(
                    BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES_STEPS,
                    null,
                    mSelectionClause,
                    mSelectionArgs,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("GetRecipeStepsLoader", "parse failed" + e);
            return null;
        }

        ArrayList<RecipeStepsSub>  fullSteps = new ArrayList<>();

        if (cursor.moveToFirst()){
            do {
                RecipeStepsSub recipe = new RecipeStepsSub();
                recipe.setIdSteps(cursor.getInt(cursor.getColumnIndex(
                        BackingContract.RecipeEntry.STEPS_ID)));
                recipe.setShortDescriptionSteps(cursor.getString(cursor.getColumnIndex(
                        BackingContract.RecipeEntry.STEPS_SHORT_DESCRIPTION)));
                recipe.setDescriptionSteps(cursor.getString(cursor.getColumnIndex(
                        BackingContract.RecipeEntry.STEPS_DESCRIPTION)));
                recipe.setVideoUrlSteps(cursor.getString(cursor.getColumnIndex(
                        BackingContract.RecipeEntry.STEPS_VIDEO_URL)));
                fullSteps.add(recipe);
            }while (cursor.moveToNext());
        }
        mContext = null;
        return  fullSteps;
    }
}
