package com.example.rcarb.backingapp.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class RecipeInfoParent implements Parcelable{

    //memeber variables for Id key and value.
    private int mIdValue;

    //memeber varaibelr for name key and value.
    private String mNameValue;

    //Member variables for ingredients key and value.
    private boolean mIngredientArrayFound = false;
    private final List<RecipeIngredientsSub> mSubIngredientArray = new ArrayList<>();

    //Member variables for steps key and values.
    private boolean mStepsArrayFound = false;
    private final List<RecipeStepsSub> mStepsArray= new ArrayList<>();

    //Member variables for servings.
    private double mServingsValue;

    //Memeber variables for image key and value.
    private String mImageValue;




    //Constructor
    public RecipeInfoParent() {
    }


    public void setIdValue(int value){
        mIdValue = value;
    }
    public int getIdValue(){
        return mIdValue;
    }


    //Getter and setter for the name key and name value.
    public void setNameValue(String string){
        mNameValue = string;
    }
    public String getNameValues(){
        return mNameValue;
    }


    //Getter and setter for ingredients boolean and array.
    public void setIngredientsBoolean(@SuppressWarnings("SameParameterValue") boolean value){
        mIngredientArrayFound = value;
    }
    public boolean getIngredientBoolean(){
        return mIngredientArrayFound;
    }

    public void setIngredientArray(ArrayList<RecipeIngredientsSub> object){
        mSubIngredientArray.addAll(object);
    }

    public List<RecipeIngredientsSub> getIngredientsArrayValues(){
        return mSubIngredientArray;
    }


    //Getter and setter for steps key and value.
    public void setStepsArrayFound(@SuppressWarnings("SameParameterValue") boolean value){
        mStepsArrayFound = value;
    }
    public boolean getStepsArrayFound(){
        return mStepsArrayFound;
    }

    public void setStepsArray(ArrayList<RecipeStepsSub> object){
        mStepsArray.addAll(object);
    }

    public List<RecipeStepsSub> getStepsArrayValues(){
        return mStepsArray;
    }


    //Getter and Setter for servings key and value.
    public void setServingsValue(double servingSize){
        mServingsValue = servingSize;
    }
    public double getServingsValues(){
        return mServingsValue;
    }


    //Getter and setter for image key and value.
    public void setImageValue(String string){
        if (string.equals("")){
            mImageValue = "none";
        }
        mImageValue = string;
    }
    public String getImageValues(){
        if (mImageValue.equals("")){
            return "none";
        }
        return mImageValue;
    }

    //Method to write boolean to parcel
    private static void writeBooleam (Parcel destination, boolean value){
        if (destination!= null){
            destination.writeInt(value ? 0 : 1);
        }
    }

    //Method to read parcelm for parcel
    private static boolean readBoolean(Parcel in){
        //noinspection SimplifiableIfStatement
        if (in != null){
            return in.readInt() == 1 ? true: false;
        }
        return false;
    }

    //Overrided imported methods.

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIdValue);
        dest.writeString(mNameValue);
        writeBooleam(dest, mIngredientArrayFound );
        writeBooleam(dest, mStepsArrayFound);
        dest.writeDouble(mServingsValue);
        dest.writeString(mImageValue);

        dest.writeTypedList(mSubIngredientArray);
        dest.writeTypedList(mStepsArray);
    }
    //Getter and setter for key id and key value.

    private RecipeInfoParent(Parcel in) {
        mIdValue = in.readInt();
        mNameValue = in.readString();
        mIngredientArrayFound = readBoolean(in);
        mStepsArrayFound = readBoolean(in);
        mServingsValue = in.readDouble();
        mImageValue = in.readString();

        in.readTypedList(mSubIngredientArray, RecipeIngredientsSub.CREATOR);
        in.readTypedList(mStepsArray, RecipeStepsSub.CREATOR);
    }

    //PArcel Creator
    public static final Creator<RecipeInfoParent> CREATOR = new Creator<RecipeInfoParent>() {
        @Override
        public RecipeInfoParent createFromParcel(Parcel in) {
            return new RecipeInfoParent(in);
        }

        @Override
        public RecipeInfoParent[] newArray(int size) {
            return new RecipeInfoParent[size];
        }
    };

}





