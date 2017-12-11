package com.example.rcarb.backingapp;


import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.rcarb.backingapp.Network.CheckConnection;


public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks{

    private int CHECK_NETWORK_CONNECTION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_loading);
        getLoaderManager().initLoader(CHECK_NETWORK_CONNECTION, null, this);

        //Prepare the loader


    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        new AsyncTaskLoader(this) {
            Context context = getContext();

            @Override
            public Object loadInBackground() {
                boolean isConeexted = CheckConnection.checkConnection(context);
                return isConeexted;
            }
        };
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}

