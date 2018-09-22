package com.manmeet.bakeit.utils;

import android.content.SharedPreferences;

import com.manmeet.bakeit.pojos.Recipe;

public interface OnRecipeClickListener {
    void onRecipeSelected(Recipe recipe, SharedPreferences sharedPreference);
}
