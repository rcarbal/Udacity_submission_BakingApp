package com.example.rcarb.backingapp.Data;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.rcarb.backingapp.R;
import com.example.rcarb.backingapp.RecipeWidgetProvider;
import com.example.rcarb.backingapp.UserInterface.GetImageResourceId;
import com.example.rcarb.backingapp.Utilities.RecipeInfoParent;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RecipeWidgetService extends IntentService {

    public RecipeWidgetService() {
        super("RecipeWidgetService");
    }

    public final static String GET_RECIPES_STORED = "get_recipes";
    public final static String UPDATE_RECIPE_WIDGET = "update_widget";

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (GET_RECIPES_STORED.equals(action)) {
                handleGetRecipes();
            }else if (UPDATE_RECIPE_WIDGET.equals(action)){
                handleUpdateWidgetWithRecipes();
            }
        }
    }

    //Gets all the recipes and updates the widgets images
    private void handleUpdateWidgetWithRecipes() {
        Cursor cursor = null;
        String[] projectionArray = new String[2];
        projectionArray[0] = BackingContract.RecipeEntry.RECIPES_ID;
        projectionArray[1] = BackingContract.RecipeEntry.RECIPES_NAME;
        try {
            cursor = getContentResolver().query(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES,
                    projectionArray,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously retrieve data");
            e.printStackTrace();
        }
        //default imageres
        int imageres;
        //Get the values from the cursor
        RecipeInfoParent parent = new RecipeInfoParent();
        assert cursor != null;
        if (cursor.moveToFirst()){
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_ID));
                parent.setIdValue(recipeId);
                String name = cursor.getString(cursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_NAME));
                parent.setNameValue(name);
            }while (cursor.moveToNext());
        }
        cursor.close();
        imageres = GetImageResourceId.getImgaeId(parent.getIdValue());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        //Trigger data update to handle gridview widget and force data refresh.
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_gridview);

        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, imageres, appWidgetIds, parent);

    }

    //This method is just to test the service and query the database.
    private void handleGetRecipes() {

        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES,
                   null,
                    null,
                    null,
                    null,
                    null);
        } catch (Exception e) {
            Log.e(TAG, "Failed to asynchronously retrieve data");
            e.printStackTrace();
        }
        //Get the values from the cursor
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection") List<String>
                recipes = new ArrayList<>();
        assert cursor != null;
        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_NAME));
                int recipeId = cursor.getInt(cursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_ID));
                recipes.add(name);
            }while (cursor.moveToNext());
        }

        cursor.close();


    }

    public static void startGetRecipesForWidgetUpdate(Context context){
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(UPDATE_RECIPE_WIDGET);
        context.startService(intent);
    }
}
