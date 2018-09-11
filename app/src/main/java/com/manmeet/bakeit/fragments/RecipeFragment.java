package com.manmeet.bakeit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manmeet.bakeit.R;
import com.manmeet.bakeit.adapters.RecipeAdapter;
import com.manmeet.bakeit.pojos.Recipe;
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
    public List<Recipe> recipeList;
    private boolean mTwoPane;
    public RecipeAdapter recipeAdapter;
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
        recipeAdapter.getItemCount();
        recipeRecyclerView.setAdapter(recipeAdapter);

        getRecipeList();
        return view;
    }

    public void getRecipeList() {
        Call<ArrayList<Recipe>> call = apiInterface.getRecipeList();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                if (response.body() != null) {
                    recipeList.addAll(response.body());
                    Log.i("RecipeList - ",recipeList.toString());
                    recipeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v(TAG, "onFailure() "+ t.toString());

            }
        });
    }
    /*// TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
