package com.example.rcarb.backingapp;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.rcarb.backingapp.LoadersAndAsyncTasks.GetIngredientsAsyncTaskLoader;
import com.example.rcarb.backingapp.UserInterface.SetIngredientsAdaptor;
import com.example.rcarb.backingapp.Utilities.RecipeIngredientsSub;

import java.util.ArrayList;

/**
 * Created by rcarb on 1/2/2018.
 */

public class PresentIngredientsActivity extends AppCompatActivity {
    String mRecipeTitle;
    int mREcipeId;
    //RecyclerView
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;

    private static final int LOAD_RECYCLER_INGREDIENTS_LOADER = 7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredients_layout);

        mRecyclerView = findViewById(R.id.ingredients_rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        getIntentData();
        parseForIngredients();
    }

    private void getIntentData(){
        Bundle extras = getIntent().getExtras();
        mRecipeTitle = extras.getString("recipe_title", "Recipe");
        mREcipeId = extras.getInt("recipe_id");
        setTitle(mRecipeTitle+ ": Ingredients");
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

    //<-------------------------------Loader Callbacks------------------------------------------->
    private LoaderManager.LoaderCallbacks<ArrayList<RecipeIngredientsSub>> getIngredients =
            new LoaderManager.LoaderCallbacks<ArrayList<RecipeIngredientsSub>>() {
                @Override
                public Loader<ArrayList<RecipeIngredientsSub>> onCreateLoader(int id, Bundle args) {
                    return new GetIngredientsAsyncTaskLoader(PresentIngredientsActivity.this,
                            mREcipeId);
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<RecipeIngredientsSub>> loader, ArrayList<RecipeIngredientsSub> data) {
                    ArrayList<RecipeIngredientsSub> array = new ArrayList<>(data);
                    //setup adaptor.
                    RecyclerView.Adapter adapter= new SetIngredientsAdaptor(array);
                    mRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<RecipeIngredientsSub>> loader) {

                }
            };
}
