package com.manmeet.bakeit.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.google.gson.Gson;
import com.manmeet.bakeit.R;
import com.manmeet.bakeit.pojos.Ingredient;
import com.manmeet.bakeit.pojos.Recipe;
import com.manmeet.bakeit.utils.ConstantUtility;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    SharedPreferences sharedPreferences;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, String recipeName, List<Ingredient> ingredientList) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        views.setTextViewText(R.id.widget_recipe_name_text_view, recipeName);
        views.removeAllViews(R.id.widget_ingredients_container);
        for (Ingredient ingredient : ingredientList) {
            RemoteViews ingredientView = new RemoteViews(context.getPackageName(),
                    R.layout.ingredient_list_item);
            ingredientView.setTextViewText(R.id.ingredient_name,
                    String.valueOf(ingredient.getIngredient()) );

            ingredientView.setTextViewText(R.id.ingredient_quantitiy,
                    String.valueOf(ingredient.getQuantity())+" "+ String.valueOf(ingredient.getMeasure()) );
            views.addView(R.id.widget_ingredients_container, ingredientView);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        sharedPreferences = context.getSharedPreferences(ConstantUtility.SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        String result = sharedPreferences.getString(ConstantUtility.WIDGET_RESULT, null);
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(result, Recipe.class);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe.getName(), recipe.getIngredients());
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

