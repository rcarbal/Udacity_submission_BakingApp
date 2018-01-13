package com.example.rcarb.backingapp.Utilities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.rcarb.backingapp.Data.BackingContract;
import com.example.rcarb.backingapp.Data.RecipeWidgetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rcarb on 12/11/2017.
 */

//Tasks that this utility class taskes care of the different task that will be ran
//by the intent service.

public class IntentServiceTasks {

    private static final String TAG = Activity.class.getName();

    //Task to insert to databse
    public static final String INSERT_TO_DATABSE ="insert-to-database";

    public static void executeTask(String action, ArrayList<RecipeInfoParent> arrayList,
                                   Context context){
        if (INSERT_TO_DATABSE.equals(action)){
            insertToDatabse(arrayList, context);
        }
    }

    //Method that will insert to that database.
    private static void insertToDatabse(ArrayList<RecipeInfoParent> arrayList, Context context){
        //Run the method that will write to the database.
        if (arrayList!= null){
            //Loop throgh the recipes and insert them to the proper databse.
            for (int i = 0; i <arrayList.size(); i++){
                //Get the first recipe.
                RecipeInfoParent recipe = arrayList.get(i);
                //The content values that will go into the Recipe table.
                ContentValues cv = new ContentValues();
                cv.put(BackingContract.RecipeEntry.RECIPES_ID, recipe.getIdValue());
                cv.put(BackingContract.RecipeEntry.RECIPES_NAME, recipe.getNameValues());
                cv.put(BackingContract.RecipeEntry.RECIPES_SERVINGS, recipe.getServingsValues());
                cv.put(BackingContract.RecipeEntry.RECIPES_IMAGE, recipe.getImageValues());

                Uri recipesUri = context.getContentResolver().insert(BackingContract.RecipeEntry.
                        BASE_CONTENT_URI_RECIPES, cv);

                if (recipesUri == null){
                    Log.e("Recipe insert", "Uri insert unssuccessful");
                }
                // The recipe ingredients are inserted using the foreign key.
                List<RecipeIngredientsSub> subIngredients = recipe.getIngredientsArrayValues();
                for (int x = 0; x < subIngredients.size(); x++) {
                    //@object(x) contain the data to set for current parent recipe.
                    RecipeIngredientsSub object = subIngredients.get(x);
                    //Variables that are extracted from the RecipeIngredientsSub.
                    double quantity = object.getQuantityValue();
                    String measure = object.getMeasureValue();
                    String ingredient = object.getIngredientValue();

                    //The content values that will be inserted to the Ingredients table.
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BackingContract.RecipeEntry.INGREDIENTS_QUANTITY, quantity);
                    contentValues.put(BackingContract.RecipeEntry.INGREDIENTS_MEASURE, measure);
                    contentValues.put(BackingContract.RecipeEntry.INGREDIENT_INGREDIENTS, ingredient);
                    contentValues.put(BackingContract.RecipeEntry.INGREDINETS_RECIPE_ID, recipe.getIdValue());

                    //Insert into databse
                    Uri ingredientsUri = context.getContentResolver().insert(BackingContract.RecipeEntry.
                            BASE_CONTENT_URI_RECIPES_INGREDIENTS, contentValues);
                    if (ingredientsUri == null){
                        Log.e(TAG, "insert unsuccessful");
                    }
                }
                //The current Recipe object's steps are inserted.
                    List<RecipeStepsSub> steps = recipe.getStepsArrayValues();
                for (int a = 0; a < steps.size(); a++){
                    //@steps(a) contain the steps for the current Recipe.
                    RecipeStepsSub recipeSteps = steps.get(a);
                    //Variables for the steps of the current recipes.
                    int stepsId = steps.get(a).getIdSteps();
                    String shortDescription = steps.get(a).getShortDescriptionSteps();
                    String description = steps.get(a).getDescriptionSteps();
                    String videoUrl = steps.get(a).getVideoUrlSteps();
                    String thumbnail = steps.get(a).getThumbnailSteps();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BackingContract.RecipeEntry.STEPS_ID, stepsId);
                    contentValues.put(BackingContract.RecipeEntry.STEPS_SHORT_DESCRIPTION, shortDescription);
                    contentValues.put(BackingContract.RecipeEntry.STEPS_DESCRIPTION, description);
                    contentValues.put(BackingContract.RecipeEntry.STEPS_VIDEO_URL, videoUrl);
                    contentValues.put(BackingContract.RecipeEntry.STEPS_THUMBNAIL_URL, thumbnail);
                    contentValues.put(BackingContract.RecipeEntry.STEPS_RECIPE_ID, recipe.getIdValue());

                    //Insert to database
                    Uri stepsUri = context.getContentResolver().insert(BackingContract.RecipeEntry.
                            BASE_CONTENT_URI_RECIPES_STEPS, contentValues);
                    if (stepsUri == null){
                        Log.e(TAG, "insert to steps table unsuccessful");
                    }
                }

            }
        }


     Intent intent = new Intent("database-ready");
     context.sendBroadcast(intent);
    }
}
