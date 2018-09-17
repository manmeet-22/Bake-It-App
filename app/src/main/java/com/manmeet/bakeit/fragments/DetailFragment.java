package com.manmeet.bakeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manmeet.bakeit.VideoActvity;
import com.manmeet.bakeit.utils.ConstantUtility;
import com.manmeet.bakeit.R;
import com.manmeet.bakeit.adapters.IngredientAdapter;
import com.manmeet.bakeit.adapters.StepAdapter;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.utils.OnStepClickListener;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements OnStepClickListener{
    private RecyclerView ingredientRecyclerView;
    private RecyclerView stepRecyclerView;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;
    private Gson gson;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ingredientRecyclerView = view.findViewById(R.id.ingredient_recycler_view);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientList = new ArrayList<Ingredient>();

        stepRecyclerView = view.findViewById(R.id.step_recycler_view);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepList = new ArrayList<Step>();

        if ( isNetworkConnected()){
            Bundle bundle = getArguments();
            String ingredients = bundle.getString(ConstantUtility.INTENT_INGREDIENT_KEY);
            String steps = bundle.getString(ConstantUtility.INTENT_STEP_KEY);

            gson = new Gson();
            ingredientList = gson.fromJson(ingredients,
                    new TypeToken<List<Ingredient>>(){}.getType());
            ingredientAdapter = new IngredientAdapter(getContext(), ingredientList);
            ingredientRecyclerView.setAdapter(ingredientAdapter);
            ingredientAdapter.notifyDataSetChanged();

            stepList = gson.fromJson(steps,
                    new TypeToken<List<Step>>() {
                    }.getType());
            stepAdapter = new StepAdapter(getContext(), stepList);
            stepAdapter.setOnClick(this);
            stepRecyclerView.setAdapter(stepAdapter);
            stepAdapter.notifyDataSetChanged();
        }
        return view;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    @Override
    public void onStepSelected(Context context, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
        Intent intent = new Intent(context, VideoActvity.class);
        intent.putExtra(ConstantUtility.INTENT_SHORT_DESCRIPTION_KEY, shortDescription);
        intent.putExtra(ConstantUtility.INTENT_DESCRIPTION_KEY, description);
        intent.putExtra(ConstantUtility.INTENT_VIDEO_URL_KEY, videoUrl);
        intent.putExtra(ConstantUtility.INTENT_THUMBNAIL_KEY, thumbnailUrl);
        context.startActivity(intent);
    }
}
