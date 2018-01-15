package com.example.rcarb.backingapp.UserInterface;

//Convinience class that returns the id of an image, following the procedure
//in the Udacity course on widgets.

import com.example.rcarb.backingapp.R;

public class GetImageResourceId {
    public static int getImgaeId(int recipeId) {
                   int recipeIdvalue;
            switch (recipeId) {
                case 1:
                    recipeIdvalue = R.drawable.nutellapie;
                    break;
                case 2:
                    recipeIdvalue = R.drawable.brownies;
                    break;
                case 3:
                    recipeIdvalue = R.drawable.yellow_cake;
                    break;
                case 4:
                    recipeIdvalue = R.drawable.cheesecake;
                    break;
                default:
                    recipeIdvalue = R.drawable.no_picture;
                }
            return recipeIdvalue;
    }
}
