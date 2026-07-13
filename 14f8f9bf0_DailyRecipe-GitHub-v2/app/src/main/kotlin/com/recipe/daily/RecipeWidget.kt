package com.recipe.daily

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.util.Calendar

class RecipeWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val recipe = getRecipeOfTheDay()
            val views = RemoteViews(context.packageName, R.layout.widget_recipe)

            views.setTextViewText(R.id.widgetTitle, recipe.title)
            views.setTextViewText(R.id.widgetDesc, recipe.description)
            views.setTextViewText(R.id.widgetSeason, "${recipe.season.uppercase()} RECIPE")

            // Seasonal background colors
            val backgroundColor = when (recipe.season) {
                "Winter" -> 0xFF1E3A8A.toInt()  // Deep blue
                "Spring" -> 0xFF14532D.toInt()  // Deep green
                "Summer" -> 0xFF7C2D12.toInt()  // Deep terracotta/red
                else     -> 0xFF78350F.toInt()  // Deep amber (Autumn)
            }
            views.setInt(R.id.widgetBackground, "setBackgroundColor", backgroundColor)

            // Tap to open the recipe detail
            val intent = Intent(context, RecipeDetailActivity::class.java).apply {
                putExtra("RECIPE_ID", recipe.id)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                recipe.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widgetBackground, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getRecipeOfTheDay(): Recipe {
            val calendar = Calendar.getInstance()
            val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
            val month = calendar.get(Calendar.MONTH)

            val season = when (month) {
                Calendar.DECEMBER, Calendar.JANUARY, Calendar.FEBRUARY -> "Winter"
                Calendar.MARCH, Calendar.APRIL, Calendar.MAY -> "Spring"
                Calendar.JUNE, Calendar.JULY, Calendar.AUGUST -> "Summer"
                else -> "Autumn"
            }

            val seasonalRecipes = RecipeData.recipes.filter { it.season.equals(season, ignoreCase = true) }
            val index = dayOfYear % seasonalRecipes.size
            return seasonalRecipes[index]
        }
    }
}
