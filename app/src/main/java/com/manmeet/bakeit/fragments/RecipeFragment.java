package com.manmeet.bakeit.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.manmeet.bakeit.DetailActivity;
import com.manmeet.bakeit.MainActivity;
import com.manmeet.bakeit.R;
import com.manmeet.bakeit.adapters.RecipeAdapter;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Recipe;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.retrofit.ApiBuilder;
import com.manmeet.bakeit.retrofit.ApiInterface;
import com.manmeet.bakeit.utils.ConstantUtility;
import com.manmeet.bakeit.utils.OnRecipeClickListener;
import com.manmeet.bakeit.utils.SimpleIdlingResource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class RecipeFragment extends Fragment implements OnRecipeClickListener {
    private RecyclerView recipeRecyclerView;
    private List<Recipe> recipeList;
    private boolean mTwoPane;
    private TextView recipeNoNetwork;
    private RecipeAdapter recipeAdapter;
    private ApiInterface apiInterface;
    private SimpleIdlingResource simpleIdlingResource;


    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        apiInterface = ApiBuilder.createService(ApiInterface.class);
        simpleIdlingResource = (SimpleIdlingResource) ((MainActivity) getActivity()).getIdlingResource();
        if (simpleIdlingResource != null){
            simpleIdlingResource.setIdleState(false);
        }

        recipeNoNetwork = view.findViewById(R.id.recipe_no_network);
        recipeRecyclerView = view.findViewById(R.id.recipe_recycler_view);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getContext(), recipeList);
        recipeAdapter.setOnClick(this);
        recipeRecyclerView.setAdapter(recipeAdapter);
        if ( isNetworkConnected()){
            recipeRecyclerView.setVisibility(View.VISIBLE);
            recipeNoNetwork.setVisibility(View.GONE);
            getRecipeList();
        } else {
            recipeRecyclerView.setVisibility(View.GONE);
            recipeNoNetwork.setVisibility(View.VISIBLE);
        }
        return view;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();

    }

    public void getRecipeList() {
        Call<ArrayList<Recipe>> call = apiInterface.getRecipeList();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.body() != null) {
                    mTwoPane = MainActivity.isTabletView();
                    if (mTwoPane){
                        recipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    }else {
                        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                    recipeList.addAll(response.body());
                    recipeAdapter.notifyDataSetChanged();
                    simpleIdlingResource.setIdleState(true);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v(TAG, "onFailure() "+ t.toString());

            }
        });
    }

    @Override
    public void onRecipeSelected(Recipe recipe, SharedPreferences sharedPreference) {
                ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
                ArrayList<Step> stepList = new ArrayList<Step>();
                if (recipe.getIngredients() != null) {
                    ingredientList.addAll(recipe.getIngredients());
                }
                if (recipe.getSteps() != null) {
                    stepList.addAll(recipe.getSteps());
                }
                Intent intent = new Intent(getContext(), DetailActivity.class);
                Gson gson = new Gson();
                String ingredientJson = gson.toJson(ingredientList);
                String stepJson = gson.toJson(stepList);
                String recipeName = recipe.getName();
                intent.putExtra(ConstantUtility.INTENT_RECIPE_NAME_KEY, recipeName);
                intent.putExtra(ConstantUtility.INTENT_INGREDIENT_KEY, ingredientJson);
                intent.putExtra(ConstantUtility.INTENT_STEP_KEY, stepJson);
                String recipeJson = gson.toJson(recipe);
                sharedPreference.edit().putString(ConstantUtility.WIDGET_RESULT, recipeJson).apply();
                getContext().startActivity(intent);
    }
}
