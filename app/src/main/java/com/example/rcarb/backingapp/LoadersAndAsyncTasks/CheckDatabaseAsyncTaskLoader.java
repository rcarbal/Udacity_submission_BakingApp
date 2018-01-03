package com.example.rcarb.backingapp.LoadersAndAsyncTasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.rcarb.backingapp.Data.BackingContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by rcarb on 12/28/2017.
 */

public class CheckDatabaseAsyncTaskLoader extends AsyncTaskLoader<List<String>> {
    public CheckDatabaseAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List loadInBackground() {

        Cursor cursor = null;
        String[] projectionArray = new String[1];
        projectionArray[0] = BackingContract.RecipeEntry.RECIPES_NAME;
        try {
           cursor = getContext().getContentResolver().query(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES,
                    projectionArray,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously retrieve data");
            e.printStackTrace();
        }
        //Get the values from the cursor
        List<String> recipes = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_NAME));
                recipes.add(name);
            }while (cursor.moveToNext());
        }

     cursor.close();
     return recipes;
    }

}
