package com.example.rcarb.backingapp.Data;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.rcarb.backingapp.Utilities.IntentServiceTasks;
import com.example.rcarb.backingapp.Utilities.RecipeInfoParent;

import java.util.ArrayList;

public class IntentServiceSQL extends IntentService {

    public IntentServiceSQL() {
        super("IntentServiceSQL");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Get the action of the intent.
        assert intent != null;
        String action = intent.getAction();
        ArrayList<RecipeInfoParent> arrayList = intent.getParcelableArrayListExtra("parcel");
        Context context = getApplicationContext();
        IntentServiceTasks.executeTask(action, arrayList, context);
    }
}
