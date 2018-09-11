package com.manmeet.bakeit.retrofit;

import com.manmeet.bakeit.pojos.Recipe;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipeList();
}
