package com.manmeet.bakeit;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.manmeet.bakeit.fragments.RecipeFragment;
import com.manmeet.bakeit.utils.SimpleIdlingResource;

public class MainActivity extends AppCompatActivity {

    private static boolean mTabletView;

    private SimpleIdlingResource simpleIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.recipe_list_tab_container) == null) {
            mTabletView = false;
        } else {
            mTabletView = true;
        }
        getIdlingResource();
    }
    public static boolean isTabletView() {
        return mTabletView;
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (simpleIdlingResource == null) {
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }
}