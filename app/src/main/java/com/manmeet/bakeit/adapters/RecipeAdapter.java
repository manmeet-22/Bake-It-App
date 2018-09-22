package com.manmeet.bakeit.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.manmeet.bakeit.DetailActivity;
import com.manmeet.bakeit.R;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Recipe;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.utils.ConstantUtility;
import com.manmeet.bakeit.utils.OnRecipeClickListener;
import com.manmeet.bakeit.utils.OnStepClickListener;

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
    private SharedPreferences sharedPreference;
    private OnRecipeClickListener onRecipeClickListener;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
        sharedPreference = context.getSharedPreferences(ConstantUtility.SHARED_PREFERENCE,
                Context.MODE_PRIVATE);

    }

    @Override
    public RecipeAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeAdapter.RecipeViewHolder holder, final int position) {
        final Recipe recipe = recipeList.get(position);
        holder.recipeName.setText(recipe.getName());
        holder.recipeServing.setText(recipe.getServings().toString());
        holder.recipeImage.setImageResource(ConstantUtility.RECIPE_IMAGE.get(position));
        holder.recipeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecipeClickListener.onRecipeSelected(recipe,sharedPreference);
            }
        });
    }

    public void setOnClick(OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }


    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeName;
        private CardView recipeCardView;
        private TextView recipeServing;
        private ImageView recipeImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.main_recipe_name);
            recipeCardView = itemView.findViewById(R.id.recipe_list_cardview);
            recipeServing = itemView.findViewById(R.id.main_recipe_serving);
            recipeImage = itemView.findViewById(R.id.main_recipe_image);
        }
    }
}
