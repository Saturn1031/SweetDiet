package com.narae.sweetdiet

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.narae.sweetdiet.databinding.ItemRecipeFoodBinding

class RecipeViewHolder(val binding: ItemRecipeFoodBinding): RecyclerView.ViewHolder(binding.root)

class RecipeAdapter (val datas:MutableList<myJsonFoodsRecipe>): RecyclerView.Adapter<RecyclerView.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecipeViewHolder(ItemRecipeFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as RecipeViewHolder).binding
        val model = datas!![position]

        Log.d("mobileapp", "model.food_Nm : ${model.food_Nm}")

        binding.txtFood.text = model.food_Nm
        if (model.food_Image_Address.equals("")) {
            binding.imgFood.setImageResource(R.drawable.logo)
        } else {
            Glide.with(binding.root)
                .load(model.food_Image_Address)
                .override(400, 300)
                .into(binding.imgFood)
        }
    }

}