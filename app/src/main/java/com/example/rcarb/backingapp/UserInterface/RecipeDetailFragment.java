package com.example.rcarb.backingapp.UserInterface;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rcarb.backingapp.R;
import com.example.rcarb.backingapp.Utilities.RecipeStepsSub;

import java.util.ArrayList;

public class RecipeDetailFragment extends Fragment
        implements SingleRecipeDetailAdaptor.OnItemClicked {

    private RecipeDetailFragment.SendToActivity mSendCallback;

    public interface SendToActivity {
        void send(RecipeStepsSub recipe, int recipeId);
    }

    //Fragments imageView
    private ImageView mRecipeImage;
    //The recycler view thaty will be used.
    private RecyclerView mRecyclerView;
    //The fragments layoutmanager
    private LinearLayoutManager mLayoutManager;
    //Adaptor that will be used
    private SingleRecipeDetailAdaptor mAdaptor;
    //Manditory empty constuctor
    private Cursor mCursor;
    private String mRecipeTitle;
    //Stored Steps
    ArrayList<RecipeStepsSub> mStoredSteps;
    private int mRecipeId;


    public RecipeDetailFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //This is to make sure that the host activity has the interface implemented. Throws an
        //exception if it doesnt/
        try {
            mSendCallback = (SendToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement SendToActivity interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgument();
    }

    //onCreateView is called when the fragment is created for display similar to onCreate.

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_list_fragment, container, false);
        mRecipeImage = rootView.findViewById(R.id.detail_fragment_imageview);
        getImageResource();
        mRecyclerView = rootView.findViewById(R.id.fragment_container_rv);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.hasFixedSize();
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdaptor = new SingleRecipeDetailAdaptor(mCursor, this);
        Cursor cursor = mCursor;
        mRecyclerView.setAdapter(mAdaptor);
        return rootView;
    }

    private void getArgument() {
        mRecipeTitle = this.getArguments().getString("image");
    }

    private void getImageResource() {
        switch (mRecipeTitle) {

            case "Nutella Pie":
                mRecipeImage.setImageResource(R.drawable.nutellapie);
                break;
            case "Brownies":
                mRecipeImage.setImageResource(R.drawable.brownies_pic);
                break;
            case "Yellow Cake":
                mRecipeImage.setImageResource(R.drawable.yellow_cake);
                break;
            case "Cheesecake":
                mRecipeImage.setImageResource(R.drawable.cheesecake_pic);
                break;
            default:
                mRecipeImage.setImageResource(R.drawable.no_picture);
                break;
        }
    }


    //Setthe the cursor method.
    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public void onDestroy() {
        if (mCursor!= null){
            mCursor.close();
        }
        super.onDestroy();

    }


    @Override
    public void onItemClicked(RecipeStepsSub recipe, int position) {
        if (position == 0) {
            mSendCallback.send(null, position);

        } else if (position > 0) {
            mSendCallback.send(recipe, position);
        }
    }

}