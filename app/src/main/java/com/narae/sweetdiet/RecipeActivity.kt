package com.narae.sweetdiet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.narae.sweetdiet.databinding.ActivityRecipeBinding

class RecipeActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val recipe = bundle!!.getSerializable("recipe") as myJsonItemsRecipe

        binding.txtMain.text = recipe.fd_Nm
        binding.txtRecipe.text = recipe.ckry_Sumry_Info
        binding.foodRecyclerView.adapter = RecipeAdapter(recipe.food_List)
        binding.foodRecyclerView.layoutManager = LinearLayoutManager(this)
        Log.d("mobileapp", "food_List : ${recipe.food_List}")
    }
}