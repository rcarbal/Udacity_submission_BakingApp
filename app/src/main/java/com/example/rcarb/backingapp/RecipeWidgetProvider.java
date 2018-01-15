package com.example.rcarb.backingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.rcarb.backingapp.Data.BackingContract;
import com.example.rcarb.backingapp.Data.RecipeWidgetService;
import com.example.rcarb.backingapp.Utilities.GridWidgetService;
import com.example.rcarb.backingapp.Utilities.RecipeInfoParent;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    @SuppressWarnings("WeakerAccess")
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int imgRes, int appWidgetId, RecipeInfoParent parent) {

        //Get current width to decide over a single recipe vs a gridview
        Bundle option = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int  width = option.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);

        RemoteViews rv;
        if (width < 250){
            rv = getSingleRecipeRemoteView(context, imgRes, parent);
        }else {
            rv = getGridRemoteViews(context, appWidgetId);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, rv);

    }

    private static RemoteViews getSingleRecipeRemoteView(Context context, int imgRes, RecipeInfoParent parent) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        //Pending intent that will launch the app.
        Intent intent;
        if (parent.getIdValue() == BackingContract.RecipeEntry.INVALID_RECIPE){
            intent = new Intent(context, MainActivity.class);
        }else{
            intent = new Intent(context, MainActivity.class);
            intent.putExtra("intent", true);
            intent.putExtra(MainActivity.WIDGET_RECIPE_ID, parent.getIdValue());
            intent.putExtra(MainActivity.WIDGET_RECIPE_NAME, parent.getNameValues());
        }
        intent.setAction(RecipeWidgetService.UPDATE_RECIPE_WIDGET);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setImageViewResource(R.id.recipe_widget, imgRes);
        //Set click handlers to the view and launch the widget.
        views.setOnClickPendingIntent(R.id.recipe_widget, pendingIntent);
        return views;
    }
    //Set up RemoteViews for GriodView.

    private static RemoteViews getGridRemoteViews(Context context, int appWidgetId){


        //Set the GridView intent  to act  as the adaptor for the GridView.
        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId );

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.windget_recipe_gridview);
        views.setRemoteAdapter(R.id.widget_gridview, intent);

        //Set the Activity to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent  = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT) ;
        //Set the pending intent as a Pending intent Template on the grid_view.
        views.setPendingIntentTemplate(R.id.widget_gridview, appPendingIntent);



        views.setEmptyView(R.id.widget_gridview, R.id.empty_view);
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RecipeWidgetService.startGetRecipesForWidgetUpdate(context);
    }
    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int imgRes, int[]appWidgetIds, RecipeInfoParent parent){
        for (int appWidgetId: appWidgetIds){
            updateAppWidget(context, appWidgetManager, imgRes, appWidgetId, parent);
        }
    }
    //This method is called everytime the widget dimenssions changed, such as resizing the widget.
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RecipeWidgetService.startGetRecipesForWidgetUpdate(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

