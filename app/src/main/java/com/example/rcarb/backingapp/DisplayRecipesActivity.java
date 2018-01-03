package com.example.rcarb.backingapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.rcarb.backingapp.LoadersAndAsyncTasks.SetupChildRecipeAsyncTaskLoader;
import com.example.rcarb.backingapp.UserInterface.SingleRecipeDetailAdaptor;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;

/**
 * Created by rcarb on 12/21/2017.
 */

public class DisplayRecipesActivity extends AppCompatActivity
        implements SingleRecipeDetailAdaptor.OnItemClicked{

    private static final String TAG = Activity.class.getName();
;
    //get the recycler view
    RecyclerView mRecyclerView;
    //get the layout manager
    LinearLayoutManager mLayoutManager;
    //Recipe Id memeber variable.
    private int mRecipeId;
    private String mRecipeTitle;
    private ImageView mImageView;


    //Loader Id for the initial RecyclerView loader
    private final static int LOAD_RECYCLER_VIEW_LOADER = 10;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_recipes_layout);

        mImageView = findViewById(R.id.detail_recipe_photo);
        //Get the recyclerview
        mRecyclerView = findViewById(R.id.rv_single_recipe);
        //It will not change size
        mRecyclerView.setHasFixedSize(true);
        //Set the layout manager.
        mLayoutManager = new LinearLayoutManager(this);

        //Attach the layout manager to the recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Extract the intent data.
        mRecipeId = getIntentData();
        //initiate the loader for the RecyclerView.
        loadRecyclerView();


    }


    private int getIntentData() {
        Intent intent = getIntent();
        int recipeId = intent.getIntExtra("recipe_id", -1);
        mRecipeTitle = intent.getStringExtra("recipe_name");
        setTitle(mRecipeTitle);
        if (recipeId == -1){
            //TODO intent wasnt passed through
        }
        switch (mRecipeTitle){
            case "Nutella Pie":
                mImageView.setImageResource(R.drawable.nutellapie);
                break;
            case "Brownies":
                mImageView.setImageResource(R.drawable.brownies);
                break;
            case "Yellow Cake":
                mImageView.setImageResource(R.drawable.yellow_cake);
                break;
            case "Cheesecake":
                mImageView.setImageResource(R.drawable.cheesecake);
                break;
            default:
                mImageView.setImageResource(R.drawable.no_picture);
                break;
        }
        return recipeId;
    }

    //The callbacks foro the loaders recycler view.
    private void loadRecyclerView() {
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id",mRecipeId);
        LoaderManager loaderManager = getLoaderManager();
        Loader<Cursor> loadCursor = loaderManager.getLoader(LOAD_RECYCLER_VIEW_LOADER);

        //If the loader manager does not exist then initialize it.
        if (loadCursor == null){
            loaderManager.initLoader(LOAD_RECYCLER_VIEW_LOADER, bundle, loadRecyclerView);
        }else {
            loaderManager.restartLoader(LOAD_RECYCLER_VIEW_LOADER, bundle, loadRecyclerView);
        }

    }


//  <------------------- Loader callbacks-------------------------->
    //Load recycler view loader
    LoaderManager.LoaderCallbacks<Cursor> loadRecyclerView =
        new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {

                return new SetupChildRecipeAsyncTaskLoader(DisplayRecipesActivity.this, args);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                if (data == null){
                    Log.e(TAG, "Step details cursor null");
                    return;
                }
                //Setup the Adaptor to the recyclerview.
                RecyclerView.Adapter adapter= new SingleRecipeDetailAdaptor(data, DisplayRecipesActivity.this);
                mRecyclerView.setAdapter(adapter);

            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        };

    @Override
    public void onItemClicked(RecipeStepsSub recipe, int position) {

        if (position == 0){
            Intent intent = new Intent(DisplayRecipesActivity.this,
                    PresentIngredientsActivity.class);
            intent.putExtra("recipe_title", mRecipeTitle);
            intent.putExtra("recipe_id", mRecipeId);
            startActivity(intent);
        }else {

            RecipeStepsSub res = recipe;

            Intent intent = new Intent(DisplayRecipesActivity.this,
                    RecipeDetailsActivity.class);
            //Puts parceble into intent.
            intent.putExtra("step_info", res);
            intent.putExtra("recipe_title", mRecipeTitle);
            intent.putExtra("recipe_id", mRecipeId);
            startActivity(intent);
        }
    }

// <---------------------Loader calbacks end ---------------------->

}
