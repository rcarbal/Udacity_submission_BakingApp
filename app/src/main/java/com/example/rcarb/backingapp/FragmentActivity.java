package com.example.rcarb.backingapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rcarb.backingapp.LoadersAndAsyncTasks.GetIngredientsAsyncTaskLoader;
import com.example.rcarb.backingapp.LoadersAndAsyncTasks.SetupChildRecipeAsyncTaskLoader;
import com.example.rcarb.backingapp.UserInterface.IngredientsDetailFragment;
import com.example.rcarb.backingapp.UserInterface.RecipeDetailFragment;
import com.example.rcarb.backingapp.UserInterface.StepsDetailFragment;
import com.example.rcarb.backingapp.Utilities.RecipeIngredientsSub;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;

import java.util.ArrayList;

public class FragmentActivity extends AppCompatActivity
        implements RecipeDetailFragment.SendToActivity{

    private static final int LOAD_RECYCLER_VIEW_LOADER = 8;
    private static final int LOAD_RECYCLER_INGREDIENTS_LOADER = 9;
    private static final int LOAD_RECIPE_STEPS_LOADER = 10;
    private static final String TAG = FragmentActivity.class.getSimpleName();
    private int mRecipeId;
    private String mRecipeTitle;
    private ArrayList<RecipeIngredientsSub> mIngredients;
    private ArrayList<RecipeStepsSub> mStoredSteps;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getIntentData();
        getDataCursor();
        parseForIngredients();
//        getReciepSteps();
        setContentView(R.layout.two_pane);


    }
    //Gets intent data
    private void getIntentData() {
        Intent intent = getIntent();
        mRecipeId = intent.getIntExtra("recipe_id", -1);
        mRecipeTitle = intent.getStringExtra("recipe_name");
        setTitle(mRecipeTitle);
    }

    //The callbacks foro the loaders recycler view.
    private void getDataCursor() {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", mRecipeId);
        LoaderManager loaderManager = getLoaderManager();
        Loader<Cursor> loadCursor = loaderManager.getLoader(LOAD_RECYCLER_VIEW_LOADER);

        //If the loader manager does not exist then initialize it.
        if (loadCursor == null) {
            loaderManager.initLoader(LOAD_RECYCLER_VIEW_LOADER, bundle, getCuror);
        } else {
            loaderManager.restartLoader(LOAD_RECYCLER_VIEW_LOADER, bundle, getCuror);
        }

    }

    //Parses the databse for the ingredients
    private void parseForIngredients(){
        LoaderManager loaderManager = getLoaderManager();
        Loader<ArrayList<RecipeIngredientsSub>> id = loaderManager.getLoader(LOAD_RECYCLER_INGREDIENTS_LOADER);
        //If the loader does not exist, restart loader.
        if (id == null){
            loaderManager.initLoader(LOAD_RECYCLER_INGREDIENTS_LOADER, null, getIngredients);
        }else{
            loaderManager.restartLoader(LOAD_RECYCLER_INGREDIENTS_LOADER, null, getIngredients);
        }
    }
//    //Initialize Loader to store steps.
//    public void getReciepSteps(){
//        LoaderManager loaderManager = getLoaderManager();
//        Loader<ArrayList<RecipeIngredientsSub>> loader= loaderManager.getLoader(LOAD_RECIPE_STEPS_LOADER);
//        if (loader == null){
//            loaderManager.initLoader(LOAD_RECIPE_STEPS_LOADER, null, getOtherSteps);
//        }
//
//    }
    //<-----------------------------LOADER CALLBACKS----------------------------------------------->
private final LoaderManager.LoaderCallbacks<Cursor> getCuror =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                    return new SetupChildRecipeAsyncTaskLoader(FragmentActivity.this, args);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    if (data == null){
                        Log.e(TAG, "Step details cursor null");
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("image", mRecipeTitle);

                    RecipeDetailFragment recipeFragment = new RecipeDetailFragment();
                    recipeFragment.setArguments(bundle);
                    recipeFragment.setCursor(data);
                    //Fragment transaction
                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.detail_list_fragment, recipeFragment);
                    transaction.commit();
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            };

    //Ingredients to get Loader
    private final LoaderManager.LoaderCallbacks<ArrayList<RecipeIngredientsSub>> getIngredients =
            new LoaderManager.LoaderCallbacks<ArrayList<RecipeIngredientsSub>>() {
                @Override
                public Loader<ArrayList<RecipeIngredientsSub>> onCreateLoader(int id, Bundle args) {
                    return new GetIngredientsAsyncTaskLoader(FragmentActivity.this,
                            mRecipeId);
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<RecipeIngredientsSub>> loader, ArrayList<RecipeIngredientsSub> data) {
                    mIngredients = new ArrayList<>(data);
                    //Fragment transaction manager
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", data);
                    //Set recipe fragment
                    IngredientsDetailFragment recipeFragment = new IngredientsDetailFragment();
                    android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    recipeFragment.setArguments(bundle);
                    transaction.add(R.id.swap_fragment_container, recipeFragment);
                    transaction.commit();
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<RecipeIngredientsSub>> loader) {

                }
            };
//    //Loader to change when button is clicked.
//    LoaderManager.LoaderCallbacks<ArrayList<RecipeStepsSub>> getOtherSteps =
//            new LoaderManager.LoaderCallbacks<ArrayList<RecipeStepsSub>>() {
//                @Override
//                public Loader<ArrayList<RecipeStepsSub>> onCreateLoader(int id, Bundle args) {
//                    return new GetRecipeStepsLoader(FragmentActivity.this, mRecipeId);
//                }
//
//                @Override
//                public void onLoadFinished(Loader<ArrayList<RecipeStepsSub>> loader, ArrayList<RecipeStepsSub> data) {
//                    if (data == null) {
//                        Toast.makeText(FragmentActivity.this, "All steps not saved.",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    mStoredSteps = data;
//                }
//                @Override
//                public void onLoaderReset(Loader<ArrayList<RecipeStepsSub>> loader) {
//
//                }
//            };


    @Override
    public void send(RecipeStepsSub recipe, int recipeId) {
        if (recipeId == 0){
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("data", mIngredients);
            //Set recipe fragment
            IngredientsDetailFragment recipeFragment = new IngredientsDetailFragment();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            recipeFragment.setArguments(bundle);
            //Since selecting the ingredients again will not change anything we just replace the fragment.
            transaction.replace(R.id.swap_fragment_container, recipeFragment);
            transaction.commit();
        }else if (recipeId >0){
            @SuppressWarnings("UnnecessaryLocalVariable") RecipeStepsSub current = recipe;
            //Bundle to be sent to the steps fragment.
            Bundle bundle = new Bundle();
            bundle.putString("description", current.getDescriptionSteps());
            bundle.putParcelable("recipe", recipe);
            //Get a new fragment
            StepsDetailFragment fragment =new StepsDetailFragment();
            //Get the transaction anager
            fragment.setArguments(bundle);
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.swap_fragment_container, fragment);
            transaction.commit();
        }

    }
}
