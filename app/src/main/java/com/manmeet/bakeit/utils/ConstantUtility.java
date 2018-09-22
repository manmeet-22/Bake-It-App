package com.manmeet.bakeit.utils;

import com.manmeet.bakeit.R;

import java.util.ArrayList;
import java.util.List;

public class ConstantUtility {

    public static final String INTENT_RECIPE_NAME_KEY = "recipeName";
    public static final String INTENT_INGREDIENT_KEY = "ingredient";
    public static final String INTENT_STEP_KEY = "step";
    public static final String INTENT_SHORT_DESCRIPTION_KEY = "shortDescription";
    public static final String INTENT_DESCRIPTION_KEY = "description";
    public static final String INTENT_VIDEO_URL_KEY = "videoUrl";
    public static final String INTENT_THUMBNAIL_KEY = "thumbnailUrl";
    public static final String INTENT_TAB_VIEW_KEY = "tabletView";

    public static final String KEY_VISIBILITY_VIDEO_PLACEHOLDER = "videoPlaceholderVisibility";
    public static final String KEY_VISIBILITY_EXO_PLAYER = "exoPlayerVisibility";

    public static final String BOOLEAN_PLAY_WHEN_READY = "playWhenReady";

    public static final String MEDIA_POSITION = "mediaPosition";

    public static final String SHARED_PREFERENCE = "widgetPref";
    public static final String WIDGET_RESULT = "recipeList";

    public static final List<Integer> RECIPE_IMAGE = new ArrayList<Integer>() {{
        add(R.drawable.recipe_nutella_pie);
        add(R.drawable.recipe_brownies);
        add(R.drawable.recipe_yellow_cake);
        add(R.drawable.recipe_cheese_cake);
    }};

    public static final String KEY_ROTATION_DETAIL_ACTIVITY = "detailRotation";
    public static final String KEY_ROTATION_VIDEO_ACTIVITY = "videoRotation";

    public static final String DETAIL_RECYCLER_VIEW_STATE = "detailRecyclerViewScroll";
}
