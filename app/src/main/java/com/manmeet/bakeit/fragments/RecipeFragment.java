package com.manmeet.bakeit.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.manmeet.bakeit.R;
import com.manmeet.bakeit.adapters.RecipeAdapter;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Recipe;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.retrofit.ApiBuilder;
import com.manmeet.bakeit.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class RecipeFragment extends Fragment {
    public RecyclerView recipeRecyclerView;
    String resultJson;
    private List<Recipe> recipeList;
    private boolean mTwoPane;
    private RecipeAdapter recipeAdapter;
    private ApiInterface apiInterface;

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        apiInterface = ApiBuilder.createService(ApiInterface.class);
        recipeRecyclerView = view.findViewById(R.id.recipe_recycler_view);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getContext(), recipeList);
        recipeRecyclerView.setAdapter(recipeAdapter);
        if ( isNetworkConnected()){
            getRecipeList();
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
                    recipeList.addAll(response.body());
                    recipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v(TAG, "onFailure() "+ t.toString());

            }
        });
    }
}
