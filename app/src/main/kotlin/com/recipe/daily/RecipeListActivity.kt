package com.recipe.daily

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RecipeListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var spinnerSeason: Spinner
    private var selectedSeason: String = "All"
    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        listView = findViewById(R.id.listViewRecipes)
        spinnerSeason = findViewById(R.id.spinnerSeason)

        val seasons = arrayOf("All", "Winter", "Spring", "Summer", "Autumn")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, seasons)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSeason.adapter = spinnerAdapter

        adapter = RecipeAdapter(this, RecipeData.recipes)
        listView.adapter = adapter

        spinnerSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedSeason = seasons[position]
                filterRecipes()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val clickedRecipe = adapter.getItem(position) as Recipe
            val intent = Intent(this, RecipeDetailActivity::class.java).apply {
                putExtra("RECIPE_ID", clickedRecipe.id)
            }
            startActivity(intent)
        }
    }

    private fun filterRecipes() {
        val filteredList = if (selectedSeason == "All") {
            RecipeData.recipes
        } else {
            RecipeData.recipes.filter { it.season.equals(selectedSeason, ignoreCase = true) }
        }
        adapter.updateData(filteredList)
    }

    private class RecipeAdapter(private val context: Context, private var list: List<Recipe>) : BaseAdapter() {

        fun updateData(newList: List<Recipe>) {
            list = newList
            notifyDataSetChanged()
        }

        override fun getCount(): Int = list.size
        override fun getItem(position: Int): Any = list[position]
        override fun getItemId(position: Int): Long = list[position].id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false)
            val recipe = list[position]

            view.findViewById<TextView>(R.id.tvItemTitle).text = recipe.title
            view.findViewById<TextView>(R.id.tvItemDesc).text = recipe.description
            view.findViewById<TextView>(R.id.tvItemSeason).text = recipe.season

            return view
        }
    }
}
