package com.manmeet.bakeit.fragments;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manmeet.bakeit.R;
import com.manmeet.bakeit.VideoActvity;
import com.manmeet.bakeit.adapters.IngredientAdapter;
import com.manmeet.bakeit.adapters.StepAdapter;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Recipe;
import com.manmeet.bakeit.pojos.Step;
import com.manmeet.bakeit.utils.ConstantUtility;
import com.manmeet.bakeit.utils.OnStepClickListener;
import com.manmeet.bakeit.widget.RecipeWidget;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements OnStepClickListener {
    private TextView recipeName;
    private RecyclerView ingredientRecyclerView;
    private RecyclerView stepRecyclerView;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private IngredientAdapter ingredientAdapter;
    private StepAdapter stepAdapter;
    private Gson gson;
    private FloatingActionButton floatingActionButton;
    private boolean mTabletView;
    private Parcelable mListState;
    private LinearLayoutManager linearLayoutManager;


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        recipeName = view.findViewById(R.id.ingredient_recipe_name);
        floatingActionButton = view.findViewById(R.id.fab);
        ingredientRecyclerView = view.findViewById(R.id.ingredient_recycler_view);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());
        ingredientList = new ArrayList<Ingredient>();

        stepRecyclerView = view.findViewById(R.id.step_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        stepRecyclerView.setLayoutManager(linearLayoutManager);
        stepRecyclerView.setItemAnimator(new DefaultItemAnimator());
        stepList = new ArrayList<Step>();

        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(ConstantUtility.DETAIL_RECYCLER_VIEW_STATE);
        }
        Bundle bundle = getArguments();
        String ingredients = bundle.getString(ConstantUtility.INTENT_INGREDIENT_KEY);
        String steps = bundle.getString(ConstantUtility.INTENT_STEP_KEY);
        String recipe = bundle.getString(ConstantUtility.INTENT_RECIPE_NAME_KEY);
        mTabletView = bundle.getBoolean(ConstantUtility.INTENT_TAB_VIEW_KEY);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity()
                        .getSharedPreferences(ConstantUtility.SHARED_PREFERENCE, Context.MODE_PRIVATE);
                Recipe recipe = gson.fromJson(sharedPreferences.getString(ConstantUtility.WIDGET_RESULT, null), Recipe.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                Bundle bundle = new Bundle();
                int appWidgetId = bundle.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                RecipeWidget.updateAppWidget(getActivity(), appWidgetManager, appWidgetId, recipe.getName(),
                        recipe.getIngredients());
                Toast.makeText(getActivity(), "Added " + recipe.getName() + " to Widget.", Toast.LENGTH_SHORT).show();

            }
        });

        recipeName.setText(recipe);
        gson = new Gson();
        ingredientList = gson.fromJson(ingredients,
                new TypeToken<List<Ingredient>>() {
                }.getType());
        ingredientAdapter = new IngredientAdapter(getContext(), ingredientList);
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        ingredientAdapter.notifyDataSetChanged();

        stepList = gson.fromJson(steps,
                new TypeToken<List<Step>>() {
                }.getType());
        stepAdapter = new StepAdapter(getContext(), stepList);
        stepAdapter.setOnClick(this);
        stepRecyclerView.setAdapter(stepAdapter);
        stepAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            //Restoring recycler view state
            linearLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //storing recycler view state
        outState.putParcelable(ConstantUtility.DETAIL_RECYCLER_VIEW_STATE, linearLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onStepSelected(Step step) {
        if (mTabletView) {
            Bundle bundle = new Bundle();
            bundle.putString(ConstantUtility.INTENT_SHORT_DESCRIPTION_KEY, step.getShortDescription());
            bundle.putString(ConstantUtility.INTENT_DESCRIPTION_KEY, step.getDescription());
            bundle.putString(ConstantUtility.INTENT_VIDEO_URL_KEY, step.getVideoURL());
            bundle.putString(ConstantUtility.INTENT_THUMBNAIL_KEY, step.getThumbnailURL());
            bundle.putBoolean(ConstantUtility.INTENT_TAB_VIEW_KEY, mTabletView);
            VideoFragment videoFragment = new VideoFragment();
            videoFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.video_container, videoFragment).commit();
        } else {
            Intent intent = new Intent(getContext(), VideoActvity.class);
            intent.putExtra(ConstantUtility.INTENT_SHORT_DESCRIPTION_KEY, step.getShortDescription());
            intent.putExtra(ConstantUtility.INTENT_DESCRIPTION_KEY, step.getDescription());
            intent.putExtra(ConstantUtility.INTENT_VIDEO_URL_KEY, step.getVideoURL());
            intent.putExtra(ConstantUtility.INTENT_THUMBNAIL_KEY, step.getThumbnailURL());
            getContext().startActivity(intent);
        }

    }

}
