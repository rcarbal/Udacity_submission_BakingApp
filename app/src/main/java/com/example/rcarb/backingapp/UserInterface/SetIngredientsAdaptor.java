package com.example.rcarb.backingapp.UserInterface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rcarb.backingapp.R;
import com.example.rcarb.backingapp.Utilities.RecipeIngredientsSub;

import java.util.ArrayList;

public class SetIngredientsAdaptor extends RecyclerView.Adapter<SetIngredientsAdaptor.IngredientHolder>{

    private final ArrayList<RecipeIngredientsSub> mIngredients;

    public SetIngredientsAdaptor(ArrayList<RecipeIngredientsSub> ingredients) {
        super();
        mIngredients = ingredients;
    }

    @Override
    public IngredientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int viewToBeInflated = R.layout.ingredients_layout_holder;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean immediatelyAttached = false;

        //Construct the view
        @SuppressWarnings("ConstantConditions")
        View view = layoutInflater.inflate(viewToBeInflated, parent, immediatelyAttached);
        return new IngredientHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(IngredientHolder holder, int position) {
        RecipeIngredientsSub recipeIngredientsSub = mIngredients.get(position);

        //get values
        double exQuantity = recipeIngredientsSub.getQuantityValue();
        String exMeasure = recipeIngredientsSub.getMeasureValue();
        String exIngredient = recipeIngredientsSub.getIngredientValue();

        //set values
        holder.quantity.setText("" + exQuantity + " "+ exMeasure +" "+ exIngredient);

    }

    @Override
    public int getItemCount() {
        if (mIngredients == null){
            return 0;
        }
        return mIngredients.size();
    }

    class IngredientHolder extends RecyclerView.ViewHolder{

        ImageView vectorImage;
        final TextView quantity;
        TextView measure;
        TextView ingredient;

        public IngredientHolder(View itemView) {
            super(itemView);

//            vectorImage = itemView.findViewById(R.id.vector_image);
            quantity = itemView.findViewById(R.id.concatenate);
//            measure = itemView.findViewById(R.id.measure);
//            ingredient = itemView.findViewById(R.id.ingredient_name);
        }
    }
}
