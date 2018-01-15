package com.example.rcarb.backingapp.LoadersAndAsyncTasks;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.rcarb.backingapp.Data.BackingContract;
public class SetupParentRecyclerViewLoader extends AsyncTaskLoader<Cursor> {

    private static final String TAG = Activity.class.getName();

    public SetupParentRecyclerViewLoader(Context context) {
        super(context);
    }


    @Override
    public Cursor loadInBackground() {
        try {
            return getContext().getContentResolver().query(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES,
                    null,
                    null,
                    null,
                    BackingContract.RecipeEntry.RECIPES_ID,
                    null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously retrieve data");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
