package com.example.rcarb.backingapp;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rcarb.backingapp.LoadersAndAsyncTasks.GetRecipeStepsLoader;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by rcarb on 12/25/2017.
 */

public class RecipeStepDetailsActivity extends AppCompatActivity {

    RecipeStepsSub mCurrentStep;
    private String mRecipeTitle;
    private int mRecipeId;
    int mStepId;
    int mIndexOfStep;
    int mSizedOfArray;
    private boolean haveParsed;
    //If there is no more steps
    private boolean mHasNextStep;
    private boolean mHasPrevious;

    ArrayList<RecipeStepsSub> mStoredSteps;


    //Loader variable
    private static final int GET_RECIPE_STEPS = 6;
    //Bandwidth meter for the trackselector.
    private static final DefaultBandwidthMeter BANDWITH_METER =
            new DefaultBandwidthMeter();

    //Exoplayer class
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayerView mPlayerViewNull;

    //State of track/media source.
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    TextView textView;
    Button mNextButton;
    Button mPreviousButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps_detail_activity);

        mPlayerView = findViewById(R.id.simpleExoPlayerView);
        mPlayerView.setVisibility(View.GONE);
        mPlayerViewNull = findViewById(R.id.simpleExoPlayerView_null);
        mPlayerViewNull.setVisibility(View.GONE);
        mCurrentStep = getIntentsInfo();
        textView = findViewById(R.id.textView);
        haveParsed = false;
        mStepId = mCurrentStep.getIdSteps();
        mStoredSteps = new ArrayList<>();
        mNextButton = findViewById(R.id.button_next);
        mNextButton.setClickable(false);
        mPreviousButton = findViewById(R.id.button_previous);
        mPreviousButton.setClickable(false);

        //Setup Ui
        setupUi();
        parseForSteps();
        setButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("mCurrentStep", mCurrentStep);
        outState.putString("mRecipeTitle", mRecipeTitle);
        outState.putInt("mRecipeId", mRecipeId);
        outState.putInt("mStepId", mStepId);
        outState.putInt("mIndexOfStep", mIndexOfStep);
        outState.putInt("sizeSteps", mSizedOfArray);
        outState.putBoolean("next", mHasNextStep);
        outState.putBoolean("previous", mHasPrevious);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentStep = savedInstanceState.getParcelable("mCurrentStep");
            mRecipeTitle = savedInstanceState.getString("mRecipeTitle");
            mRecipeId = savedInstanceState.getInt("mRecipeId");
            mStepId = savedInstanceState.getInt("mStepId");
            mIndexOfStep = savedInstanceState.getInt("mIndexOfStep");
            mSizedOfArray = savedInstanceState.getInt("sizeSteps");
            mHasNextStep = savedInstanceState.getBoolean("next");
            mHasPrevious = savedInstanceState.getBoolean("previous");
            mNextButton.setClickable(mHasNextStep);
            mPreviousButton.setClickable(mHasPrevious);
            super.onRestoreInstanceState(savedInstanceState);
            setupUi();

        }
    }

    //Gets the passed in intent
    private RecipeStepsSub getIntentsInfo() {
        Bundle intent = getIntent().getExtras();
        RecipeStepsSub recipeStepsSub = intent.getParcelable("step_info");
        mRecipeTitle = intent.getString("recipe_title");
        mRecipeId = intent.getInt("recipe_id");
        if (recipeStepsSub == null) {
            return null;
        }
        return recipeStepsSub;
    }

    //Initiates the Exoplayer
    private void initializePlayer(RecipeStepsSub currentStep) {
        if (mPlayer == null) {
            Uri uri = Uri.parse(mCurrentStep.getVideoUrlSteps());
            if (uri.toString().equals("")) {
                mPlayer = null;
                mPlayerView.setVisibility(View.GONE);
                mPlayerViewNull.setVisibility(View.VISIBLE);
            } else {
                mPlayerViewNull.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                //Track selector that contains the BANDWIDTH_METER.
                TrackSelection.Factory adaptiveTrackSelectionFactory =
                        new AdaptiveTrackSelection.Factory(BANDWITH_METER);
                mPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(this),
                        new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                        new DefaultLoadControl());

                //Set the view to the player.
                mPlayerView.setPlayer(mPlayer);

                mPlayer.setPlayWhenReady(playWhenReady);
                mPlayer.seekTo(currentWindow, playbackPosition);

                //Create the MediaSource
                MediaSource mediaSource = buildMediaSource(uri);
                mPlayer.prepare(mediaSource, true, false);
            }
        }
    }

    //Clears the player
    private void clearPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    //Method to find the current index of the current step.
    private int getIndexOfCurrentStep() {
        //for loop to find index
        int currentSIze = mStoredSteps.size();
        for (int i = 0; i < mStoredSteps.size(); i++) {
            int id = mStoredSteps.get(i).getIdSteps();
            int current = mStepId;
            if (id == current) {
                return i;
            } else {
                //TODO if the if is never found.
            }
        }
        return -1;
    }

    private void setButtons() {
        int getTotalStepIndex = mStoredSteps.size() - 1;

        mIndexOfStep = getIndexOfCurrentStep();
        //Set if there is next step
        if (mIndexOfStep < getTotalStepIndex) {
            mHasNextStep = true;
            mNextButton.setClickable(true);
        } else if (mIndexOfStep == getTotalStepIndex) {
            mHasNextStep = false;
            mNextButton.setClickable(false);
        }

        //Set if there is previous step.
        if (mIndexOfStep > 0) {
            mHasPrevious = true;
            mPreviousButton.setClickable(true);
        } else if (mIndexOfStep == 0) {
            mHasPrevious = false;
            mPreviousButton.setClickable(false);
        }


    }

    //Releases the Player
    private void releasePlayer() {
        if (mPlayer != null) {
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            playWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    //Prepare the Media Source that will be used by the player instance.
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory datasourceFactory =
                new DefaultHttpDataSourceFactory("ua", BANDWITH_METER);
        ExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();

        ExtractorMediaSource mediaSource = new ExtractorMediaSource(uri,
                datasourceFactory,
                extractorsFactory,
                null,
                null);
        return mediaSource;
    }

    //Creates full screen if the screen configuration is landscape.
    @SuppressLint("InlinedApi")
    private void fullScreenImplementation() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    //When next button is pressed.
    public void parseForSteps() {
        LoaderManager loaderManager = getLoaderManager();
        Loader<ArrayList<RecipeStepsSub>> ld = loaderManager.getLoader(GET_RECIPE_STEPS);

        //If the loader manager does not exist then initialize it.
        if (ld == null) {
            loaderManager.initLoader(GET_RECIPE_STEPS, null, getOtherSteps);
        } else {
            loaderManager.restartLoader(GET_RECIPE_STEPS, null, getOtherSteps);
        }
    }

    //On button click goes to next step
    public void nextStep(View view) {
        if (mHasNextStep) {
            if (mIndexOfStep < mSizedOfArray) {
                //Clear the player
                clearPlayer();
                //Get the next index
                int nextIndex = getIndexOfCurrentStep() + 1;
                //Get next Recipe.
                RecipeStepsSub next = mStoredSteps.get(nextIndex);
                mCurrentStep = next;
                //Initialize current step
                initializePlayer(mCurrentStep);
                mStepId = mCurrentStep.getIdSteps();
                setupUi();
                mIndexOfStep = getIndexOfCurrentStep();

                //If reach end disable the next button.
                if (mCurrentStep.getIdSteps() == mStoredSteps.get(mSizedOfArray - 1).getIdSteps()) {
                    mHasNextStep = false;
                    mNextButton.setClickable(false);
                }
                //If index is greater than 0 than has previous.
                if (mIndexOfStep > 0) {
                    mHasPrevious = true;
                    mPreviousButton.setClickable(true);
                }
            }
        }
    }

    //On button click goes to previous
    public void previousStep(View view) {
        if (mHasPrevious) {
            //Clear the player.
            clearPlayer();
            int previousIndex = getIndexOfCurrentStep() - 1;
            //Get previous recipe
            RecipeStepsSub previous = mStoredSteps.get(previousIndex);
            mCurrentStep = previous;
            //Initialize previous step.
            initializePlayer(previous);
            mStepId = mCurrentStep.getIdSteps();
            setupUi();
            ;
            mIndexOfStep = getIndexOfCurrentStep();
        }
        //If you reach the ned of the previous.
        if (mCurrentStep.getIdSteps() == mStoredSteps.get(0).getIdSteps()) {
            mHasPrevious = false;
            mPreviousButton.setClickable(false);
            mHasNextStep = true;
            mNextButton.setClickable(true);
        }
        //If index is less than the total index.
        if (mIndexOfStep != mSizedOfArray) {
            //Has next
            mHasNextStep = true;
            mNextButton.setClickable(true);
        }
    }


    //Sets up the layout
    private void setupUi() {
        textView.setText(mCurrentStep.getDescriptionSteps());
        setTitle(mRecipeTitle + ": " + mCurrentStep.getShortDescriptionSteps());
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            initializePlayer(mCurrentStep);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        //Check if the configuration is landscape.
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fullScreenImplementation();
        }
    }

    // <---------------------Loader calbacks ---------------------->

    //Loader to change when button is clicked.
    LoaderManager.LoaderCallbacks<ArrayList<RecipeStepsSub>> getOtherSteps =
            new LoaderManager.LoaderCallbacks<ArrayList<RecipeStepsSub>>() {
                @Override
                public Loader<ArrayList<RecipeStepsSub>> onCreateLoader(int id, Bundle args) {
                    return new GetRecipeStepsLoader(RecipeStepDetailsActivity.this,
                            mRecipeId);
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<RecipeStepsSub>> loader, ArrayList<RecipeStepsSub> data) {
                    if (data == null) {
                        Toast.makeText(RecipeStepDetailsActivity.this, "All steps not saved.",
                                Toast.LENGTH_SHORT).show();
                    }
                    mStoredSteps = data;
                    haveParsed = true;
                    mSizedOfArray = mStoredSteps.size();
                    setButtons();

                }

                @Override
                public void onLoaderReset(Loader<ArrayList<RecipeStepsSub>> loader) {

                }
            };


    // <---------------------Loader calbacks end ---------------------->
}

