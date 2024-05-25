package com.narae.sweetdiet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.sweetdiet.databinding.ItemFoodBinding

class JsonViewHolder(val binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root)
class JsonAdapter(val datas:MutableList<myJsonItems>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return JsonViewHolder(ItemFoodBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as JsonViewHolder).binding
        val model = datas!![position]

        binding.descKor.text = model.DESC_KOR
        binding.animalPlant.text = model.ANIMAL_PLANT
    }
}