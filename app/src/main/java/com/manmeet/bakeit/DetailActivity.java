package com.manmeet.bakeit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.manmeet.bakeit.adapters.IngredientAdapter;
import com.manmeet.bakeit.fragments.DetailFragment;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.utils.ConstantUtility;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private String steps;
    private String ingredients;
    private boolean tabletView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        if (findViewById(R.id.video_container) != null) {
            tabletView = true;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.video_container, new DetailFragment()).commit();

        }
        if (savedInstanceState == null) {
            ingredients = intent.getStringExtra(ConstantUtility.INTENT_INGREDIENT_KEY);
            steps = intent.getStringExtra(ConstantUtility.INTENT_STEP_KEY);
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantUtility.INTENT_INGREDIENT_KEY, ingredients);
            bundle.putString(ConstantUtility.INTENT_STEP_KEY, steps);
            detailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.detail_container, detailFragment)
                    .commit();
        } else{
            ingredients = intent.getStringExtra(ConstantUtility.INTENT_INGREDIENT_KEY);
            steps = intent.getStringExtra(ConstantUtility.INTENT_STEP_KEY);
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString(ConstantUtility.INTENT_INGREDIENT_KEY, ingredients);
            bundle.putString(ConstantUtility.INTENT_STEP_KEY, steps);
            detailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.detail_container, detailFragment)
                    .commit();

        }
    }
}
