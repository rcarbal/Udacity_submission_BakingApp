package com.example.rcarb.backingapp.UserInterface;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rcarb.backingapp.Data.BackingContract;
import com.example.rcarb.backingapp.R;
import com.example.rcarb.backingapp.Utilities.RecipeInfoParent;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;

/**
 * Created by rcarb on 12/25/2017.
 */

public class SingleRecipeDetailAdaptor extends RecyclerView.Adapter<SingleRecipeDetailAdaptor.CardViewHolder> {

    public interface OnItemClicked {
        void onItemClicked(RecipeStepsSub recipe, int position);
    }

    Cursor mCursor;
    private final OnItemClicked mItemClicked;

    public SingleRecipeDetailAdaptor(Cursor cursor,
                                     OnItemClicked listener) {
        mCursor = cursor;
        mItemClicked = listener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int viewToBeInflated = R.layout.display_recipes_layout_holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean immediatelyAttached = false;

        //Construct the view
        View view = layoutInflater.inflate(viewToBeInflated, parent, immediatelyAttached);
        int viewHeight = parent.getMeasuredHeight() / 6;
        view.setMinimumHeight(viewHeight);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        mCursor.moveToPosition(position);

        if (position == 0) {
            //Make first position blanc.
            holder.mTextView.setText(R.string.ingredient);
            holder.mTextView.setTypeface(Typeface.DEFAULT_BOLD);
            holder.itemView.setTag("ingredients");
            holder.setIsRecyclable(false);
        } else {
            //Get the desired indices.
            int stepsIdIndex = mCursor.getColumnIndex(BackingContract.RecipeEntry.STEPS_ID);
            int stepsShortDescription = mCursor.getColumnIndex(BackingContract.RecipeEntry.STEPS_SHORT_DESCRIPTION);
            int description = mCursor.getColumnIndex(BackingContract.RecipeEntry.STEPS_DESCRIPTION);
            int videoUrl = mCursor.getColumnIndex(BackingContract.RecipeEntry.STEPS_VIDEO_URL);
            int thumbnail = mCursor.getColumnIndex(BackingContract.RecipeEntry.STEPS_THUMBNAIL_URL);
            ;


            //Move the cursor to the proper position


            //Determine the values of wanted product.
            int idOfStep = mCursor.getInt(stepsIdIndex);
            String shortDes = mCursor.getString(stepsShortDescription);
            String descript = mCursor.getString(description);
            String urlVideo = mCursor.getString(videoUrl);
            String resThumbNail = mCursor.getString(thumbnail);

            //Setup the textview
            holder.mTextView.setText(shortDes);
            holder.itemView.setTag(urlVideo);

            holder.steps.setShortDescriptionSteps(shortDes);
            holder.steps.setDescriptionSteps(descript);
            holder.steps.setVideoUrlSteps(urlVideo);
            holder.steps.setThumbnailSteps(resThumbNail);
            holder.steps.setIdSteps(idOfStep);
        }
    }


    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    class CardViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mTextView;
        RecipeStepsSub steps;


        public CardViewHolder(View itemView) {
            super(itemView);

            //Cast the textview
            steps = new RecipeStepsSub();
            mTextView = itemView.findViewById(R.id.rv_steps_vh);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //get adaptor position
            int position = getAdapterPosition();
            if (position == 0) {
                int tag = position;
                String text = "ingredients";
                mItemClicked.onItemClicked(null, position);
            } else {

                RecipeStepsSub resSteps = steps;
                mItemClicked.onItemClicked(resSteps, position);
            }
        }

    }
}
