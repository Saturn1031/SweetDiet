package com.narae.sweetdiet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.sweetdiet.databinding.ItemRecipeBinding

class JsonViewHolderRecipe(val binding: ItemRecipeBinding): RecyclerView.ViewHolder(binding.root)

class JsonAdapterRecipe(val datas:MutableList<myJsonItemsRecipe>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return JsonViewHolderRecipe(ItemRecipeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as JsonViewHolderRecipe).binding
        val model = datas!![position]

        binding.fdNm.text = model.fd_Nm
        var foodNames = ""
        for (i in 0 until model.food_List.size) {
            if (i == 0) {
                foodNames += model.food_List[i].food_Nm.split(", ")[0]
            } else {
                foodNames += ", " + model.food_List[i].food_Nm.split(", ")[0]
            }
        }
        binding.foodNm.text = foodNames
    }

}