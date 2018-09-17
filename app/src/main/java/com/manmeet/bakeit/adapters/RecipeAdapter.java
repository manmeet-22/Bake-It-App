package com.manmeet.bakeit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.manmeet.bakeit.utils.ConstantUtility;
import com.manmeet.bakeit.DetailActivity;
import com.manmeet.bakeit.R;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Recipe;
import com.manmeet.bakeit.pojos.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private Context context;
    private List<Recipe> recipeList;
    private String servings;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private Intent intent;
    private Gson gson;
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
        Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());
        ingredientList = new ArrayList<Ingredient>();
        stepList = new ArrayList<Step>();
        if (recipe.getIngredients()!=null){
            ingredientList.addAll(recipe.getIngredients());
        }
        if (recipe.getSteps()!=null) {
            stepList.addAll(recipe.getSteps());
        }
        holder.recipeLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                gson = new Gson();
                String ingredientJson = gson.toJson(ingredientList);
                String stepJson = gson.toJson(stepList);
                Log.i("RecipeAdapter", "onItemClick: Position - "+"\n"+ "ingredient - "+ingredientJson);
                intent.putExtra(ConstantUtility.INTENT_INGREDIENT_KEY, ingredientJson);
                intent.putExtra(ConstantUtility.INTENT_STEP_KEY, stepJson);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder  {
        private TextView recipeName;
        private LinearLayout recipeLinearLayout;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.main_recipe_name);
            recipeLinearLayout = itemView.findViewById(R.id.recipe_list_linear_layout);
           // recipeLinearLayout.setOnClickListener(this);
        }
/*
        @Override
        public void onClick(View v) {

        }*/
    }
}
