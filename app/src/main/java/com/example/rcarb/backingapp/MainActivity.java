package com.example.rcarb.backingapp;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rcarb.backingapp.Data.IntentServiceSQL;
import com.example.rcarb.backingapp.Data.RecipeWidgetService;
import com.example.rcarb.backingapp.LoadersAndAsyncTasks.CheckConnectionLoader;
import com.example.rcarb.backingapp.LoadersAndAsyncTasks.CheckDatabaseAsyncTaskLoader;
import com.example.rcarb.backingapp.LoadersAndAsyncTasks.GetRecipesLoader;
import com.example.rcarb.backingapp.LoadersAndAsyncTasks.SetupParentRecyclerViewLoader;
import com.example.rcarb.backingapp.UserInterface.MainRecipeAdaptor;
import com.example.rcarb.backingapp.Utilities.IntentServiceTasks;
import com.example.rcarb.backingapp.Utilities.RecipeInfoParent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Boolean>,
        MainRecipeAdaptor.OnItemClicked {

    @SuppressLint("SdCardPath")
    private static final String DB_FULL_PATH = "/data/data/com.example.rcarb.backingapp/databases/BakingAppRecipes.db";
    private static final String SAVED_LAYOUT_MANAGER = "linerarLayoutManager";
    //Constant to be for widget.
    public static final String WIDGET_RECIPE_ID = "resId";
    public static final String WIDGET_RECIPE_NAME = "resName";


    //Int for the loader that checks for internet connectuion
    private final static int CHECK_NETWORK_CONNECTION = 1;
    private final static int GET_JSON_DATA = 2;
    private final static int SETUP_ACTIVITY = 3;
    private final static int CHECK_DATABSE_FOR_RECIPES = 4;

    //Intent Filter foro when the database is ready.
    private IntentFilter mDatabseReadyIntentFilter;
    //Instance variable for the Broadcast Receiver for when the databse is ready.
    private BroadcastReceiver mDatabaseReadyReciever;
    //Adaptor
    private RecyclerView mRecyclerView;
    //Layout Manager
    private LinearLayoutManager mLayoutManager;
    private boolean mDatabase = false;
    private ArrayList<RecipeInfoParent> mRecipes;
    private Parcelable mLayoutState;
    private RecyclerView.Adapter mAdaptor;
    private boolean mTwoPane;

    private String mWidgetRecipeName;
    private int mWidgetRecipeId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check if the device is a tablet.
        if (findViewById(R.id.framelayout_tablet) != null) {
            mTwoPane = true;
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            //Set the layout manager.
            mLayoutManager = new LinearLayoutManager(this);
            mTwoPane = false;
        }


        mLayoutState = new Bundle();
        // Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

        //Broadcast receiver instantiate
        mDatabaseReadyReciever = new DatabaseReadyReceiver();

        //intent filter and set action
        mDatabseReadyIntentFilter = new IntentFilter();

        mDatabseReadyIntentFilter.addAction("database-ready");

        //Get the recyclerview
        mRecyclerView = findViewById(R.id.recycler_view);
        //It will not change size
        mRecyclerView.setHasFixedSize(true);
        //Attach the layout manager to the recyclerview
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Checks if there is already a databse in the internal storage.
        mDatabase = checkDataBaseExists();
        checkIfIntentStarted();
        //Runs the method to check for internet.
        //If there is connection then the JSON data will be downloadeed.
        checkInternetConnection();


    }

    //Set Global variables if activity was started by an intent.
    private void checkIfIntentStarted() {
        if (getIntent().getBooleanExtra("intent", false)) {
            mWidgetRecipeId = getIntent().getIntExtra(WIDGET_RECIPE_ID, -1);
            mWidgetRecipeName = getIntent().getStringExtra(WIDGET_RECIPE_NAME);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mLayoutState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SAVED_LAYOUT_MANAGER, mLayoutState);
        // Toast.makeText(this, "onSavedInstance", Toast.LENGTH_SHORT).show();
    }


    //Checks to see if a contract already exists
    private boolean checkDataBaseExists() {
        SQLiteDatabase checkDB;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    //Initiate the AsyncTask loader that will check for internet connection.
    private void checkInternetConnection() {
        LoaderManager loaderManager = getLoaderManager();
        Loader<Boolean> checkConnection = loaderManager.getLoader(CHECK_NETWORK_CONNECTION);
        if (checkConnection == null) {
            getLoaderManager().initLoader(CHECK_NETWORK_CONNECTION, null, this);
        } else {
            loaderManager.restartLoader(CHECK_NETWORK_CONNECTION, null, this);
        }
    }

    //Method to setup the Recycler view on the main

    /**
     * This is the LOADER callbacks that check for INTERNET CONNECTION.
     */

    @Override
    public Loader<Boolean> onCreateLoader(int id, Bundle args) {
        return new CheckConnectionLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
        if (data) {
            LoaderManager loaderManager = getLoaderManager();
            Loader<ArrayList<RecipeInfoParent>> getLoader = loaderManager.getLoader(GET_JSON_DATA);
            if (getLoader == null) {
                getLoaderManager().initLoader(GET_JSON_DATA, null, getJsonCallbacks);
            }else {
                loaderManager.restartLoader(GET_JSON_DATA, null,getJsonCallbacks );
            }
        } else //noinspection ConstantConditions
            if (!data) {
            Toast.makeText(this, "There is no internet connection", Toast.LENGTH_LONG).show();
            //If there is no connection load the recipes from the database
            LoaderManager loaderManager = getLoaderManager();
            Loader<Cursor> loaderCheck = loaderManager.getLoader(SETUP_ACTIVITY);
            if (loaderCheck == null){
                loaderManager.initLoader(SETUP_ACTIVITY, null, setupRecyclerView);
                }else{
                loaderManager.restartLoader(SETUP_ACTIVITY, null, setupRecyclerView);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }

    /***
     * These are the LOADER callbacks for getting the JSON information and returning a
     */
    //Loader for getting the JSON data, write SQLiteDatabse

    private final LoaderManager.LoaderCallbacks<ArrayList<RecipeInfoParent>> getJsonCallbacks =
            new LoaderManager.LoaderCallbacks<ArrayList<RecipeInfoParent>>() {
                @Override
                public Loader<ArrayList<RecipeInfoParent>> onCreateLoader(int id, Bundle args) {
                    return new GetRecipesLoader(MainActivity.this);
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<RecipeInfoParent>> loader, ArrayList<RecipeInfoParent> data) {

                    mRecipes = data;
                    @SuppressWarnings("UnnecessaryLocalVariable") ArrayList<RecipeInfoParent> results = data;
                    if (mDatabase) {
                        LoaderManager loaderManager = getLoaderManager();
                        Loader<List<String>> checkDtb = loaderManager.getLoader(CHECK_DATABSE_FOR_RECIPES);
                        if (checkDtb == null) {
                            //Check if newly downloaded recipes are already saved in the database.
                            getLoaderManager().initLoader(CHECK_DATABSE_FOR_RECIPES, null, checkDatabase);
                        }else{
                            loaderManager.restartLoader(CHECK_DATABSE_FOR_RECIPES, null, checkDatabase);
                        }
                    } else {
                        //When the GetRecipesLoader is done then it will run the intent service to
                        //write the returned array into the database.
                        Intent writeArrayToDatabase = new Intent(MainActivity.this, IntentServiceSQL.class);
                        writeArrayToDatabase.setAction(IntentServiceTasks.INSERT_TO_DATABSE);
                        writeArrayToDatabase.putParcelableArrayListExtra("parcel", results);
                        startService(writeArrayToDatabase);

                    }

                }

                @Override
                public void onLoaderReset(Loader<ArrayList<RecipeInfoParent>> loader) {

                }
            };
    //Loader that checks if databse contains downloaded recipes.
    private final LoaderManager.LoaderCallbacks<List<String>> checkDatabase =
            new LoaderManager.LoaderCallbacks<List<String>>() {
                @Override
                public Loader<List<String>> onCreateLoader(int id, Bundle args) {
                    return new CheckDatabaseAsyncTaskLoader(MainActivity.this);
                }

                @Override
                public void onLoadFinished(Loader<List<String>> loader, List<String> data) {
                    //We have a list of recipes in the database.
                    @SuppressWarnings("UnnecessaryLocalVariable") List<String> namesRetrieved = data;
                    HashMap<String, Boolean> confirmed = new HashMap<>();
                    if (namesRetrieved.size() > 0) {
                        String compareString = getDownloadedRecipeNames();
                        for (int x = 0; x < namesRetrieved.size(); x++) {
                            Boolean contains = compareString.contains(namesRetrieved.get(x));
                            confirmed.put(namesRetrieved.get(x), contains);
                        }
                    }
                    //TODO id namesRetieved is 0.
                    if (!confirmed.containsValue(false)) {
                        RecipeWidgetService.startGetRecipesForWidgetUpdate(MainActivity.this);
                        Intent intent = new Intent("database-ready");
                        sendBroadcast(intent);
                    }
                    //TODO if contains the value of false
                }


                @Override
                public void onLoaderReset(Loader<List<String>> loader) {

                }
            };

    //LODAER callbacks for setting up MainActiviy RecyclerView
    private final LoaderManager.LoaderCallbacks<Cursor> setupRecyclerView =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    return new SetupParentRecyclerViewLoader(MainActivity.this);
                }

                @Override
                public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                    if (data == null) {
                        //TODO is cursor is null
                        Toast.makeText(MainActivity.this, "No recipes in databse, please" +
                                " connect to internet and restart app.", Toast.LENGTH_SHORT).show();
                    }
                    mAdaptor = new MainRecipeAdaptor(data, MainActivity.this);
                    mRecyclerView.setAdapter(mAdaptor);
                }

                @Override
                public void onLoaderReset(Loader<Cursor> loader) {

                }
            };


    //Get the names of the downloaded recipes.
    private String getDownloadedRecipeNames() {
        String names = "";
        for (int i = 0; i < mRecipes.size(); i++) {
            //noinspection StringConcatenationInLoop
            names = names + ", " + mRecipes.get(i).getNameValues();
        }
        return names;
    }

    //Interface method that retrieves the clicked viws tag.
    @Override
    public void onItemClick(int recipeId,
                            String text,
                            String contentDescription,
                            ImageView imageView) {
        //Start the next activity.

        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                this,
                imageView,
                imageView.getTransitionName()).toBundle();
        Intent intent;
        if (mTwoPane) {
            intent = new Intent(MainActivity.this, FragmentActivity.class);

        } else {
            intent = new Intent(MainActivity.this, DisplayRecipesActivity.class);
        }

        intent.putExtra("recipe_id", recipeId);
        intent.putExtra("recipe_name", text);
        intent.putExtra("content_description", contentDescription);
        startActivity(intent, bundle);
        finish();

    }

    private void launchFromIntent() {
        Intent intent;
        int id = mWidgetRecipeId;
        String name = mWidgetRecipeName;
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                this).toBundle();
        if (mTwoPane) {
            intent = new Intent(MainActivity.this, FragmentActivity.class);

        } else {
            intent = new Intent(MainActivity.this, DisplayRecipesActivity.class);
        }

        intent.putExtra("recipe_id", id);
        intent.putExtra("recipe_name", name);
        mWidgetRecipeId = -1;
        mWidgetRecipeName = "";
        startActivity(intent, bundle);
        finish();

    }

    //Braodcast Reciever that will know when databse is ready to be read.
    private class DatabaseReadyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RecipeWidgetService.startGetRecipesForWidgetUpdate(MainActivity.this);
            if (mWidgetRecipeId != -1) {
                launchFromIntent();
            } else {
                getLoaderManager().initLoader(SETUP_ACTIVITY, null, setupRecyclerView);
            }


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mDatabaseReadyReciever);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mDatabaseReadyReciever, mDatabseReadyIntentFilter);
        if (mLayoutState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutState);
        }
    }
}


