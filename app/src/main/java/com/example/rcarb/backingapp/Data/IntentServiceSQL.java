package com.example.rcarb.backingapp.Data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.example.rcarb.backingapp.Utilities.IntentServiceTasks;
import com.example.rcarb.backingapp.Utilities.RecipeInfoParent;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;

import java.util.ArrayList;

/**
 * Created by rcarb on 12/16/2017.
 */

public class IntentServiceSQL extends IntentService {

    public IntentServiceSQL() {
        super("IntentServiceSQL");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Get the action of the intent.
        String action = intent.getAction();
        ArrayList<RecipeInfoParent> arrayList = intent.<RecipeInfoParent>getParcelableArrayListExtra("parcel");
        Context context = getApplicationContext();
        IntentServiceTasks.executeTask(action, arrayList, context);
    }
}
