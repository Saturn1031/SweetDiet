package com.narae.sweetdiet

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.sweetdiet.databinding.ItemAteFoodBinding

class AteFoodViewHolder(val binding: ItemAteFoodBinding): RecyclerView.ViewHolder(binding.root)
class AteFoodAdapter(val context: Context, val datas:MutableList<FoodData>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AteFoodViewHolder(ItemAteFoodBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as AteFoodViewHolder).binding
        val model = datas[position]

        binding.ateDescKor.text = model.name
        binding.ateAnimalPlant.text = model.ANIMAL_PLANT
    }
}