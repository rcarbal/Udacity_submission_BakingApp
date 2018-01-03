package com.example.rcarb.backingapp.LoadersAndAsyncTasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rcarb on 12/11/2017.
 */

public class CheckConnectionLoader extends AsyncTaskLoader<Boolean> {

    private Context mContext;
    public CheckConnectionLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Boolean loadInBackground() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork= cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
