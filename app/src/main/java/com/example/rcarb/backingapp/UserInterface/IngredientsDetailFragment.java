package com.example.rcarb.backingapp.UserInterface;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rcarb.backingapp.R;
import com.example.rcarb.backingapp.Utilities.RecipeIngredientsSub;

import java.util.ArrayList;

/**
 * Created by rcarb on 1/8/2018.
 */

public class IngredientsDetailFragment extends Fragment {

    //Recycler view
    private RecyclerView mRecyclerView;
    //Layout Manager
    private LinearLayoutManager mLayoutManager;
    //Adaptor
    private SetIngredientsAdaptor mAdaptor;
    //Data
    private ArrayList<RecipeIngredientsSub> mData;


    public IngredientsDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.ingredients_fragment_rv, container, false);
        //Get data from bundle
        mData = this.getArguments().getParcelableArrayList("data");
        mRecyclerView = rootView.findViewById(R.id.ingredients_rv_fragment);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.hasFixedSize();
        mAdaptor = new SetIngredientsAdaptor(mData);
        mRecyclerView.setAdapter(mAdaptor);
        return rootView;

    }
}
