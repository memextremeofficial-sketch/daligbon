package com.recipe.daily

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todayRecipe = getRecipeOfTheDay()

        val tvTodayTitle = findViewById<TextView>(R.id.tvTodayTitle)
        val tvTodayDesc = findViewById<TextView>(R.id.tvTodayDesc)
        val tvTodaySeason = findViewById<TextView>(R.id.tvTodaySeason)
        val cvTodayRecipe = findViewById<CardView>(R.id.cvTodayRecipe)
        val btnViewAll = findViewById<Button>(R.id.btnViewAll)

        tvTodayTitle.text = todayRecipe.title
        tvTodayDesc.text = todayRecipe.description
        tvTodaySeason.text = "Season: ${todayRecipe.season}"

        cvTodayRecipe.setOnClickListener {
            val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                putExtra("RECIPE_ID", todayRecipe.id)
            }
            startActivity(intent)
        }

        btnViewAll.setOnClickListener {
            val intent = Intent(this, RecipeListActivity::class.java)
            startActivity(intent)
        }
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
