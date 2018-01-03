package com.example.rcarb.backingapp.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rcarb on 12/13/2017.
 */

public class RecipeIngredientsSub implements Parcelable {

    private double mQuantityValue;
    private String mMeasureValue;
    private String mIngredientValue;

    public RecipeIngredientsSub() {
    }

    protected RecipeIngredientsSub(Parcel in) {
        mQuantityValue = in.readDouble();
        mMeasureValue = in.readString();
        mIngredientValue = in.readString();
    }

    public static final Creator<RecipeIngredientsSub> CREATOR = new Creator<RecipeIngredientsSub>() {
        @Override
        public RecipeIngredientsSub createFromParcel(Parcel in) {
            return new RecipeIngredientsSub(in);
        }

        @Override
        public RecipeIngredientsSub[] newArray(int size) {
            return new RecipeIngredientsSub[size];
        }
    };

    //Getter And setter methods for quantity
    public void setQuantityValue(double value){
        mQuantityValue = value;
    }
    public double getQuantityValue(){
        return mQuantityValue;
    }

    //Getter and setter methods for measure.
    public void setMeasureValue(String string){
        mMeasureValue = string;
    }
    public String getMeasureValue(){
        return mMeasureValue;
    }

    //Getetr and setter methods for ingredients.
    public void setIngredientsValue(String string){
        mIngredientValue = string;
    }
    public String getIngredientValue(){
        return mIngredientValue;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mQuantityValue);
        dest.writeString(mMeasureValue);
        dest.writeString(mIngredientValue);
    }
}
