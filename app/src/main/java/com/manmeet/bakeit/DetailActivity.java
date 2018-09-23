package com.manmeet.bakeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manmeet.bakeit.adapters.IngredientAdapter;
import com.manmeet.bakeit.fragments.DetailFragment;
import com.manmeet.bakeit.fragments.VideoFragment;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.utils.ConstantUtility;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private String steps;
    private String ingredients;
    private String recipe;
    private boolean tabletView;
    private boolean rotationDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState != null) {
            rotationDetails = savedInstanceState.getBoolean(ConstantUtility.KEY_ROTATION_DETAIL_ACTIVITY);
        }
        if (findViewById(R.id.video_container) != null) {
            tabletView = true;
         /*   Bundle bundle = new Bundle();
            Gson gson = new Gson();
            ArrayList<Step> stepList = new ArrayList<Step>();
            stepList=gson.fromJson(steps,
                    new TypeToken<List<Step>>() {
                    }.getType());
            Step step = stepList.get(0);
            bundle.putString(ConstantUtility.INTENT_SHORT_DESCRIPTION_KEY, step.getShortDescription());
            bundle.putString(ConstantUtility.INTENT_DESCRIPTION_KEY, step.getDescription());
            bundle.putString(ConstantUtility.INTENT_VIDEO_URL_KEY, step.getVideoURL());
            bundle.putString(ConstantUtility.INTENT_THUMBNAIL_KEY, step.getThumbnailURL());
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(bundle);
         */   getSupportFragmentManager().beginTransaction()
                    .add(R.id.video_container, new VideoFragment()).commit();
        }

        Intent intent = getIntent();
        if (savedInstanceState == null) {
            ingredients = intent.getStringExtra(ConstantUtility.INTENT_INGREDIENT_KEY);
            steps = intent.getStringExtra(ConstantUtility.INTENT_STEP_KEY);
            recipe = intent.getStringExtra(ConstantUtility.INTENT_RECIPE_NAME_KEY);
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantUtility.INTENT_RECIPE_NAME_KEY, recipe);
            bundle.putString(ConstantUtility.INTENT_INGREDIENT_KEY, ingredients);
            bundle.putString(ConstantUtility.INTENT_STEP_KEY, steps);
            bundle.putBoolean(ConstantUtility.INTENT_TAB_VIEW_KEY, tabletView);
            detailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, detailFragment)
                    .commit();
            rotationDetails = true;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.getBoolean(ConstantUtility.KEY_ROTATION_DETAIL_ACTIVITY, rotationDetails);
    }
}
