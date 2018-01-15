package com.example.rcarb.backingapp.Utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class RecipeStepsSub implements Parcelable {

    private int mStepsId;
    private String mStepsShortDescription;
    private String mStepsDescription;
    private String mStepsVideoUrl;
    private String mStepsThumbnail;

    public RecipeStepsSub() {
    }



    public static final Creator<RecipeStepsSub> CREATOR = new Creator<RecipeStepsSub>() {
        @Override
        public RecipeStepsSub createFromParcel(Parcel in) {
            return new RecipeStepsSub(in);
        }

        @Override
        public RecipeStepsSub[] newArray(int size) {
            return new RecipeStepsSub[size];
        }
    };

    public void setIdSteps(int value){
        mStepsId = value;
    }
    public int getIdSteps(){
       return mStepsId;
    }
    public void setShortDescriptionSteps(String value){
        mStepsShortDescription = value;
    }
    public String getShortDescriptionSteps(){
        return mStepsShortDescription;
    }
    public void setDescriptionSteps(String value){
        mStepsDescription = value;
    }
    public String getDescriptionSteps(){
        return mStepsDescription;
    }
    public void setVideoUrlSteps(String value){
        mStepsVideoUrl = value;
    }
    public String getVideoUrlSteps(){
        return mStepsVideoUrl;
    }
    public void setThumbnailSteps(String values){
        mStepsThumbnail = values;
    }
    public String getThumbnailSteps(){
        return mStepsThumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStepsId);
        dest.writeString(mStepsShortDescription);
        dest.writeString(mStepsDescription);
        dest.writeString(mStepsVideoUrl);
        dest.writeString(mStepsThumbnail);
    }
    //getter and setter methods for the member variables.

    private RecipeStepsSub(Parcel in) {
        mStepsId = in.readInt();
        mStepsShortDescription = in.readString();
        mStepsDescription = in.readString();
        mStepsVideoUrl = in.readString();
        mStepsThumbnail = in.readString();
    }
}
