package com.narae.sweetdiet

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.narae.sweetdiet.databinding.ItemFoodBinding

class JsonViewHolder(val binding: ItemFoodBinding): RecyclerView.ViewHolder(binding.root)
class JsonAdapter(val datas:MutableList<myJsonItems>?, val checkedMeal: String, val selectedDate: String, val addMealActivity: AddMealActivity): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

        binding.btnAddMeal.setOnClickListener {
            // 로그인 이메일, 식품이름, 열량 (kcal), 탄수화물 (g), 단백질 (g), 지방 (g), 당류 (g), 나트륨 (mg), 콜레스테롤 (mg), 포화지방산 (g), 트랜스지방산 (g) 입력 시간
            // 아침, 점심, 저녁, 간식
            val data = mapOf(
                "email" to MyApplication.email,
                "name" to model.DESC_KOR,
                "calorie" to model.NUTR_CONT1,
                "carbohydrate" to model.NUTR_CONT2,
                "protein" to model.NUTR_CONT3,
                "fat" to model.NUTR_CONT4,
                "sugar" to model.NUTR_CONT5,
                "sodium" to model.NUTR_CONT6,
                "cholesterol" to model.NUTR_CONT7,
                "saturated_fat" to model.NUTR_CONT8,
                "trans_fat" to model.NUTR_CONT9,
                "date_time" to selectedDate,
                "checked_meal" to checkedMeal,
                "ANIMAL_PLANT" to model.ANIMAL_PLANT
            )
            MyApplication.db.collection("meals")
                .add(data)
                .addOnSuccessListener {
                    Log.d("mobileapp", "데이터 저장 성공")
                    Toast.makeText(holder.itemView.context, "데이터 저장 성공", Toast.LENGTH_SHORT).show()
                    addMealActivity.finish()
                }
                .addOnFailureListener {
                    Log.d("mobileapp", "데이터 저장 실패")
                    Toast.makeText(holder.itemView.context, "데이터 저장 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
}