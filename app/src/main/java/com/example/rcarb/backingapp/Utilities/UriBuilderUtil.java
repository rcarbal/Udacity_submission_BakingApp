package com.example.rcarb.backingapp.Utilities;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rcarb on 12/11/2017.
 */

public class UriBuilderUtil {

    private static final String TAG = "MainActivity";

    private static final String BASE_COOKING_URI ="https://d17h27t6h515a5.cloudfront.net";
    private static final String COOK = "topher";
    private static final String YEAR ="2017";
    private static final String MONTH = "May";
    private static final String FOLDER = "59121517_baking";
    private static final String FILE ="baking.json";


    //Builds the Uri for cooking recepies.
    public static URL buildUri(){

        Uri builtUri =
                Uri.parse(BASE_COOKING_URI).buildUpon()
                .appendPath(COOK)
                .appendPath(YEAR)
                .appendPath(MONTH)
                .appendPath(FOLDER)
                .appendPath(FILE)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e + "url not found");
            e.printStackTrace();
        }

        return url;
    }
}
