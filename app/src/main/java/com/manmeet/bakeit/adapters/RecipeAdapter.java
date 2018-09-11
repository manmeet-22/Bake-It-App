package com.manmeet.bakeit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manmeet.bakeit.R;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Recipe;
import com.manmeet.bakeit.pojos.Step;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private String servings;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private Intent intent;
    private SharedPreferences sharedPreferences;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_main_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapter.RecipeViewHolder holder, int position) {
        holder.recipeName.setText(recipeList.get(position).getName());
        Log.i("RecipeAdapter", "onBindViewHolder: "+ recipeList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        private TextView recipeName;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.main_recipe_name);
        }

    }
}
