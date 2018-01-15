package com.example.rcarb.backingapp.Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtilities {
    private static final String TAG = "MainActivity";

    //Check if String is valid Json object or array.
    public static String jsonChecker(String json) {
        try {
            JSONObject object = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                JSONArray array = new JSONArray(json);
            } catch (JSONException e1) {
                e1.printStackTrace();
                return "error";
            }
            return "array";
        }
        return "object";
    }


    //Second Checker method that attemps to make a JSONArray object out of a json string.
    //Returns null if the string cannot be converted to a JSONArray.

    public static JSONArray tryToMakeJsonArray(String stringData) {

        //Create the Json object out of the strings.
        @SuppressWarnings("UnusedAssignment")
        JSONArray jsonData = null;

        try {
            jsonData = new JSONArray(stringData);
        } catch (JSONException e) {
            Log.e(TAG, "The string data could not be converted to JSONArray " + e);
            e.printStackTrace();
            return null;
        }
        return jsonData;
    }

    /**
     * Takes in json array string and parses the objects getting that keys and the value types of
     * each of the json keys.
     *
     * @param data
     * @throws JSONException
     */
    @SuppressWarnings("JavaDoc")
    public static ArrayList<RecipeInfoParent> getJsonParentValues(JSONArray data,
                                                                  Context context)throws JSONException {
        //Check that the Json object is not empty.
        if (data != null) {


            ArrayList<RecipeInfoParent> recipeArray = new ArrayList<>();
            //Parse each JsonArray
            for (int i = 0; i < data.length(); i++) {

                RecipeInfoParent helperObject = new RecipeInfoParent();
                JSONObject object = null;
                try {
                    object = data.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            "JsonObject parse failed",
                            Toast.LENGTH_SHORT).show();
                    //TODO error if the JSON object fails, and remove toast
                }
                if (object!=null){
                    //Checks that the value type ectracted will match what we are expecting.

                    //Get Id integer value.
                    if (CheckNonNull.checkTypes(object, "id", "integer" )) {
                        helperObject.setIdValue(object.getInt("id"));
                    }else{
                        //-1 valie used id there was match.
                        helperObject.setIdValue(-1);
                    }
                    //Get the name of the reipe.
                    if (CheckNonNull.checkTypes(object, "name", "string")){
                        helperObject.setNameValue(object.getString("name"));
                    }else{
                        helperObject.setNameValue("");
                    }
                    //found an array in the ingredient key.
                    if (CheckNonNull.checkTypes(object, "ingredients", "array")){
                        helperObject.setIngredientsBoolean(true);
                    }
                    //found an array in the steps key.
                    if (CheckNonNull.checkTypes(object,"steps","array")){
                        helperObject.setStepsArrayFound(true);
                    }
                    //Get the servings key value.
                    if (CheckNonNull.checkTypes(object, "servings", "integer")){
                        helperObject.setServingsValue(object.getDouble("servings"));
                    }else {
                        helperObject.setServingsValue(-1);
                    }

                    //Get the image key value
                    if (CheckNonNull.checkTypes(object,"image","string")){
                        helperObject.setImageValue(object.getString("image"));
                    }else{
                        helperObject.setImageValue("");
                    }


                    //Parse the arrays starting with the ingredient array.
                    //Get the Array from the current object.
                    JSONArray ingredientsArray = object.getJSONArray("ingredients");
                    //loop through the arrays indexes.
                    ArrayList<RecipeIngredientsSub> ingredientsSubArray = new ArrayList<>();

                    for (int x= 0; x< ingredientsArray.length(); x++){
                        JSONObject ingredientsObject  = ingredientsArray.getJSONObject(x);
                        RecipeIngredientsSub ingredientsSub = new RecipeIngredientsSub();
                        if (ingredientsObject!= null){
                            //Get the value in the quantity key.
                            if (CheckNonNull.checkTypes(ingredientsObject, "quantity", "integer")){
                                ingredientsSub.setQuantityValue(ingredientsObject.getDouble("quantity"));
                            }else if (CheckNonNull.checkTypes(ingredientsObject, "quantity", "double")){
                                ingredientsSub.setQuantityValue(ingredientsObject.getDouble("quantity"));
                            }else{
                                ingredientsSub.setQuantityValue(-1);
                            }
                            //Get the value in the measure key
                            if (CheckNonNull.checkTypes(ingredientsObject, "measure", "string")){
                                ingredientsSub.setMeasureValue(ingredientsObject.getString("measure"));
                            }else{
                                ingredientsSub.setMeasureValue("");
                            }
                            //get the value in the ingredients
                            if (CheckNonNull.checkTypes(ingredientsObject, "ingredient", "string")){
                                ingredientsSub.setIngredientsValue(ingredientsObject.getString("ingredient"));
                            }else{
                                ingredientsSub.setIngredientsValue("");
                            }
                            ingredientsSubArray.add(ingredientsSub);
                        }

                    }
                    helperObject.setIngredientArray(ingredientsSubArray);
                    //When parsing fo the ingredients is done, begin parsing for the steps
                    //Make the JsonArray for the steps key.

                    JSONArray stepsArray = object.getJSONArray("steps");
                    ArrayList<RecipeStepsSub> recipeStepsSubArray = new ArrayList<>();
                    //Object to hold the steps.

                    //loop throught all the steps
                    for (int a= 0; a < stepsArray.length(); a++){
                        RecipeStepsSub stepsSub = new RecipeStepsSub();
                        JSONObject stepsObject = stepsArray.getJSONObject(a);
                        if (stepsObject!= null){
                            //Get the value for id key
                            if (CheckNonNull.checkTypes(stepsObject, "id", "integer")){
                                stepsSub.setIdSteps(stepsObject.getInt("id"));
                            }else{
                                stepsSub.setIdSteps(-1);
                            }
                            //Get the value of shortDescription
                            if (CheckNonNull.checkTypes(stepsObject, "shortDescription", "string")){
                                stepsSub.setShortDescriptionSteps(stepsObject.getString("shortDescription"));
                            }else{
                                stepsSub.setShortDescriptionSteps("");
                            }
                            //Get the value of description.
                            if (CheckNonNull.checkTypes(stepsObject, "description", "string")){
                                stepsSub.setDescriptionSteps(stepsObject.getString("description"));
                            }else{
                                stepsSub.setDescriptionSteps("");
                            }
                            //Get thevalue of videoURL key
                            if (CheckNonNull.checkTypes(stepsObject, "videoURL", "string")){
                                stepsSub.setVideoUrlSteps(stepsObject.getString("videoURL"));
                            }else{
                                stepsSub.setVideoUrlSteps("");
                            }
                            //Get the value of the thumbnailURL key.
                            if (CheckNonNull.checkTypes(stepsObject, "thumbnailURL", "string")){
                                stepsSub.setThumbnailSteps(stepsObject.getString("thumbnailURL"));
                            }else{
                                stepsSub.setThumbnailSteps("");
                            }
                            recipeStepsSubArray.add(stepsSub);
                        }


                    }
                    //HelperObject containing one recipie completed
                    helperObject.setStepsArray(recipeStepsSubArray);
                }
                //add the helperObject to the recipeArray
                recipeArray.add(helperObject);
            }
            return recipeArray;
        }
        /*
         * TODO need to add the if JSSONArray data is null and the ArrayList<RecipeInfoParent> Object
         * is not filled.
         */
        return null;
    }

    /*
    Since the server response contains a JSONArray, this method will extract the sub key value
    from the JSON data.
     */

}
