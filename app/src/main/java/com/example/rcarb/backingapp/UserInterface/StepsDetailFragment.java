package com.example.rcarb.backingapp.UserInterface;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rcarb.backingapp.R;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
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
 * Created by rcarb on 1/8/2018.
 */

public class StepsDetailFragment extends Fragment {

    private boolean haveParsed;
    //Bandwidth meter for the trackselector.
    private static final DefaultBandwidthMeter BANDWITH_METER =
            new DefaultBandwidthMeter();

    //Exoplayer class
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;

    //State of track/media source.
    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    private TextView textView;
    private String mDescription;
    RecipeStepsSub mCurrentStep;

    //Default constructor
    public StepsDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.steps_detail_fragment_layout, container, false);
        textView = rootView.findViewById(R.id.textView_step_fragment);
        textView.setText(mDescription);
        mPlayerView = rootView.findViewById(R.id.simpleExoPlayerView_fragment);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentBundles();

    }


    private void getFragmentBundles(){
        mDescription = this.getArguments().getString("description");
        mCurrentStep = this.getArguments().getParcelable("recipe");
    }
    //Initiates the Exoplayer
    private void initializePlayer(RecipeStepsSub currentStep) {
        if (mPlayer == null) {
            //Track selector that contains the BANDWIDTH_METER.
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWITH_METER);
            mPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                    new DefaultLoadControl());

            //Set the view to the player.
            mPlayerView.setPlayer(mPlayer);
            mPlayer.setPlayWhenReady(playWhenReady);
            mPlayer.seekTo(currentWindow, playbackPosition);

            //Create the MediaSource
            Uri uri = Uri.parse(mCurrentStep.getVideoUrlSteps());
            MediaSource mediaSource = buildMediaSource(uri);
            mPlayer.prepare(mediaSource, true, false);
        }
    }
    //Prepare the Media Source that will be used by the player instance.
    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory datasourceFactory =
                new DefaultHttpDataSourceFactory("ua", BANDWITH_METER);
        ExtractorsFactory extractorsFactory =
                new DefaultExtractorsFactory();


        return new ExtractorMediaSource(uri,
                datasourceFactory,
                extractorsFactory,
                null,
                null);
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


    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer(mCurrentStep);
    }
}
