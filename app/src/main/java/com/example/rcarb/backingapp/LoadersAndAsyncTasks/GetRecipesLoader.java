package com.example.rcarb.backingapp.LoadersAndAsyncTasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.rcarb.backingapp.Network.NetworkUtils;
import com.example.rcarb.backingapp.Utilities.JsonUtilities;
import com.example.rcarb.backingapp.Utilities.RecipeInfoParent;
import com.example.rcarb.backingapp.Utilities.UriBuilderUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class GetRecipesLoader extends AsyncTaskLoader<ArrayList<RecipeInfoParent>> {


    private static final String TAG = "MainActivity";

    private Context mContext;


    public GetRecipesLoader(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * First getting the server reponse and saving it as a String.
     *
     * @jsonString. Run the string throught the Utilities.JsonUtilities jsonChecker returns String of
     * "array", "object", "null".
     */

    @Override
    public ArrayList<RecipeInfoParent> loadInBackground() {

        //Extracts the String from the uri.
        String jsonString = "";
        try {
            jsonString = NetworkUtils.getResponseFromHttpUrl(UriBuilderUtil.buildUri());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e + "error getting string for JSON");
            //TODO need to setup a proper response for  no JSON respone String.
        }

        /**
         * Now we need to check what kind of Json the String contains.
         */
        String typeOfJson = JsonUtilities.jsonChecker(jsonString);
        ArrayList<RecipeInfoParent> allRecipes = null;
        if (typeOfJson.equals("array")) {
            JSONArray parsingData = null;
            try {
                parsingData = new JSONArray(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
                //TODO if the conversion of the string to JSONArray fails.
            }
            try {
                allRecipes = JsonUtilities.getJsonParentValues(parsingData, mContext);
            } catch (JSONException e) {
                e.printStackTrace();
                //TODO Loaders attempt to get JSON data from JSONArray.
            }
        }

        return allRecipes;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
