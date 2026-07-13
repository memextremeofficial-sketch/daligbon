package com.recipe.daily

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        val recipeId = intent.getIntExtra("RECIPE_ID", -1)
        val recipe = RecipeData.recipes.find { it.id == recipeId }

        if (recipe != null) {
            findViewById<TextView>(R.id.tvDetailTitle).text = recipe.title
            findViewById<TextView>(R.id.tvDetailSeason).text = "Season: ${recipe.season}"
            findViewById<TextView>(R.id.tvDetailDesc).text = recipe.description

            val ingredientsText = recipe.ingredients.joinToString("\n") { "• $it" }
            findViewById<TextView>(R.id.tvDetailIngredients).text = ingredientsText

            val stepsText = recipe.steps.mapIndexed { index, step -> "${index + 1}. $step" }.joinToString("\n\n")
            findViewById<TextView>(R.id.tvDetailSteps).text = stepsText
        } else {
            findViewById<TextView>(R.id.tvDetailTitle).text = "Recipe Not Found"
        }
    }
}
