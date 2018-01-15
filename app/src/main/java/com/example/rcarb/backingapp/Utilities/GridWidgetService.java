package com.example.rcarb.backingapp.Utilities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.rcarb.backingapp.Data.BackingContract;
import com.example.rcarb.backingapp.MainActivity;
import com.example.rcarb.backingapp.R;
import com.example.rcarb.backingapp.UserInterface.GetImageResourceId;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewFactory(this.getApplicationContext());
    }

    class GridRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
        @SuppressWarnings("CanBeFinal")
        Context mContext;
        Cursor mCursor;


        public GridRemoteViewFactory(Context applicationContext) {
            mContext = applicationContext;
        }

        @Override
        public void onCreate() {

        }

        //This method is called once a remoterview is created, as well as every time it's notified to
        //update it's data. Run the data query and get all items in the recipe database.
        //TO notify that the data has changed you notifyAppWidgetDataChanged().
        @Override
        public void onDataSetChanged() {
            if (mCursor != null) mCursor.close();

            mCursor = mContext.getContentResolver().query(
                    BackingContract.RecipeEntry.BASE_CONTENT_URI_RECIPES,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            if (mCursor == null) return 0;
            return mCursor.getCount();
        }

        //This is where thr binding logic goes.
        @Override
        public RemoteViews getViewAt(int position) {
            if (mCursor == null || mCursor.getCount() == 0) return null;
            mCursor.moveToPosition(position);
            //get indeces
            int idIndex = mCursor.getColumnIndex(BackingContract.RecipeEntry._ID);
            int resIdIndex = mCursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_ID);
            int resNameIndex = mCursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_NAME);

            //get Values from the index.
            long id = mCursor.getLong(idIndex);
            int resId = mCursor.getInt(resIdIndex);
            String resName = mCursor.getString(resNameIndex);

            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_provider);

            //Update the recipe image
            int imgRes = GetImageResourceId.getImgaeId(resId);

            views.setImageViewResource(R.id.recipe_widget, imgRes);
            //Intent to set the extra parameter of a id to the RemoteView.
            Bundle extras = new Bundle();
            extras.putInt(MainActivity.WIDGET_RECIPE_ID, resId);
            extras.putString(MainActivity.WIDGET_RECIPE_NAME, resName);
            extras.putBoolean("intent", true);
            Intent fillIntent = new Intent();
            fillIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.recipe_widget, fillIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}



