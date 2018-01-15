package com.example.rcarb.backingapp.LoadersAndAsyncTasks;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.os.Bundle;
import android.util.Log;

import com.example.rcarb.backingapp.Data.BackingContract;

public class SetupChildRecipeAsyncTaskLoader extends AsyncTaskLoader<Cursor>{

    private static final String TAG = Activity.class.getName();
    private final Bundle mBundle;

    public SetupChildRecipeAsyncTaskLoader(Context context, final Bundle args) {
        super(context);
        mBundle = args;

    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (mBundle == null){
            return;
        }
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        //Try to get the selected database
        int recipeId = mBundle.getInt("recipe_id");
        String mSelectionClause;
        String[] mSelectionArgs = new String[1];

        //if the recipe_id is null
        if (recipeId <= 0){
            return null;
        }else{
            mSelectionClause = BackingContract.RecipeEntry.INGREDINETS_RECIPE_ID + " =?";
            mSelectionArgs[0] = String.valueOf(recipeId);
        }

        //Try to parse the database.
        Cursor cursor = null;
        try {
            cursor = getContext().getContentResolver().query(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES_STEPS,
                    null,
                    mSelectionClause,
                    mSelectionArgs,
                    null,
                    null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Failed to retrieve data");
        }
        //return null if the try/catch failed.
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"fake", "faker"});
        matrixCursor.addRow(new Object[]{"null","null" });
        @SuppressWarnings("UnnecessaryLocalVariable")
        MergeCursor mergeCursor = new MergeCursor(new Cursor[]{matrixCursor, cursor});

        return mergeCursor;
    }

}
