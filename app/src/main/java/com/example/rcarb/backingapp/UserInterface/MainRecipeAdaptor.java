package com.example.rcarb.backingapp.UserInterface;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rcarb.backingapp.Data.BackingContract;
import com.example.rcarb.backingapp.R;

/**
 * Created by rcarb on 12/19/2017.
 */

public class MainRecipeAdaptor extends RecyclerView.Adapter<MainRecipeAdaptor.CardViewHolder> {

    private final Cursor mCursor;
    private final OnItemClicked mOnClick;


    //Interfact to send back to the MainActivity.
    public interface OnItemClicked {
        void onItemClick(int recipeId,
                         String text,
                         String contentDescription,
                         ImageView imageView);
    }

    public MainRecipeAdaptor(Cursor cursor,
                             OnItemClicked listener) {
        this.mCursor = cursor;
        this.mOnClick = listener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int viewToBeInflated = R.layout.activity_main_holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean immediatelyAttached = false;

        //Construct the view
        View view = layoutInflater.inflate(viewToBeInflated, parent, immediatelyAttached);
        int viewHeight = parent.getMeasuredHeight() / 2;
        view.setMinimumHeight(viewHeight);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        //Get the indices fot the columns
        int nameIndexColumn = mCursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_NAME);
        int recipeIdIndexColumn = mCursor.getColumnIndex(BackingContract.RecipeEntry.RECIPES_ID);

        //Move the cursor to the proper position.
        mCursor.moveToPosition(position);

        //determine the values of wanted data.
        String recipeName = mCursor.getString(nameIndexColumn);
        int recipeId = mCursor.getInt(recipeIdIndexColumn);

        //Set the tag and text of the view
        holder.itemView.setTag(recipeId);
//        holder.mTextView.setText(recipeName);

        //TODO implement the imageview in the viewholder.
        switch (recipeName) {
            case "Nutella Pie":
                holder.mImageView.setImageResource(R.drawable.nutellapie);
                break;
            case "Brownies":
                holder.mImageView.setImageResource(R.drawable.brownies);
                break;
            case "Yellow Cake":
                holder.mImageView.setImageResource(R.drawable.yellow_cake);
                break;
            case "Cheesecake":
                holder.mImageView.setImageResource(R.drawable.cheesecake);
                break;
            default:
                holder.mImageView.setImageResource(R.drawable.no_picture);
                break;
        }
        holder.mTextView.setText(recipeName);
        holder.mImageView.setContentDescription(recipeName);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    //Construct te ViewHolder object.
    class CardViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final TextView mTextView;
        final ImageView mImageView;

        public CardViewHolder(View itemView) {
            super(itemView);

            //Cast the textview.
            mTextView = itemView.findViewById(R.id.main_vh_textview);
            //initiate the imageview.
            mImageView = itemView.findViewById(R.id.cv_recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Gets the tag from the current view and on view clicked it will send the tag to the
            //MainActivity.
            int tag = (int) itemView.getTag();
            String recipe = mTextView.getText().toString();
            String contentDescription =(String)itemView.getContentDescription();
            mOnClick.onItemClick(tag,recipe, contentDescription, mImageView);// had recipe instead of null

        }
    }
}




