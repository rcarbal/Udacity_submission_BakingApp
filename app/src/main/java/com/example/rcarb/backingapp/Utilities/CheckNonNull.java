package com.example.rcarb.backingapp.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rcarb on 12/11/2017.
 */

public class CheckNonNull {


    public static void checkifItIsJson(String jsonString){

    }

    //Integer: class java.lang.Integer
    //String: class java.lang.String
    //JSONArray: class org.json.JSONArray




    //Get a value from JsonObject key and check that it is the same class as the String objectType.
    public static boolean checkTypes(JSONObject object,
                                       String id,
                                       String objectType) throws JSONException  {
        //Checks the value of the Json object to an object type.
        JSONObject data = object;
        //The string that will hold the class to compare to.
        String compare ="";



        //The possible values the compare string can be
        String integerClass = "class java.lang.Integer";
        String stringClass = "class java.lang.String";
        String arrayClass ="class org.json.JSONArray";
        String doubleClass ="class java.lang.Double";

        String a = data.get(id).getClass().toString();

        boolean match = false;

        //Checks that the object value and object type string match.
        switch (objectType){
            case "integer":
                compare =integerClass;
                break;
            case "string":
                compare = stringClass;
                break;
            case "array":
                compare = arrayClass;
                break;
            case "double":
                compare = doubleClass;
        }

        //Will return true if @String compare match with the jsson value type.
            switch (objectType){
                case "integer":
                    if (data.get(id).getClass().toString().equals(compare))
                        {
                            match = true;
                            break;
                        }
                case "string":
                    if (data.get(id).getClass().toString().equals(compare))
                    {
                        match = true;
                        break;
                    }
                case "array":
                    if (data.get(id).getClass().toString().equals(compare))
                    {
                        match = true;
                        break;
                    }
                case "double":
                    if (data.get(id).getClass().toString().equals(compare))
                    {
                        match = true;
                        break;

                    }
            }

        return match;
    }
}
