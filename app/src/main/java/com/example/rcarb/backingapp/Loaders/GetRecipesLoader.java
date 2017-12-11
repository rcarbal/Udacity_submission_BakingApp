package com.example.rcarb.backingapp.Loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.rcarb.backingapp.Network.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class GetRecipesLoader extends AsyncTaskLoader<String> {


    public GetRecipesLoader(Context context) {
        super(context);
    }

    @Override
    public String loadInBackground() {

        URL url = null;

        try {
            url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String s ="";
        try {
            s = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ss = "";
        return s;
    }

}
