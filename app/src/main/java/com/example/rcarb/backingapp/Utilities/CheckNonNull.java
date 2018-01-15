package com.example.rcarb.backingapp.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("WeakerAccess")
public class CheckNonNull {


    //Get a value from JsonObject key and check that it is the same class as the String objectType.
    public static boolean checkTypes(JSONObject object,
                                       String id,
                                       String objectType) throws JSONException  {
        //Checks the value of the Json object to an object type.
        //The string that will hold the class to compare to.
        String compare ="";



        //The possible values the compare string can be
        String integerClass = "class java.lang.Integer";
        String stringClass = "class java.lang.String";
        String arrayClass ="class org.json.JSONArray";
        String doubleClass ="class java.lang.Double";

        String a = object.get(id).getClass().toString();

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
                    if (object.get(id).getClass().toString().equals(compare))
                        {
                            match = true;
                            break;
                        }
                case "string":
                    if (object.get(id).getClass().toString().equals(compare))
                    {
                        match = true;
                        break;
                    }
                case "array":
                    if (object.get(id).getClass().toString().equals(compare))
                    {
                        match = true;
                        break;
                    }
                case "double":
                    if (object.get(id).getClass().toString().equals(compare))
                    {
                        match = true;
                        break;

                    }
            }

        return match;
    }
}
