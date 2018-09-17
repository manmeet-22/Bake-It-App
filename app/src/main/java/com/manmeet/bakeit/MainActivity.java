package com.manmeet.bakeit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.manmeet.bakeit.fragments.RecipeFragment;

public class MainActivity extends AppCompatActivity {

    private static boolean mTabletView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.recipe_list_tab_container) == null) {
            mTabletView = false;
        } else {
            mTabletView = true;
        }
    }
    public static boolean checkTabletView() {
        return mTabletView;
    }
}