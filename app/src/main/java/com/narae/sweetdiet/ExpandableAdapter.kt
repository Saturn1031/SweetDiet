package com.narae.sweetdiet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.narae.sweetdiet.databinding.ItemAccordionBinding


class ExpandableAdapter(
    private val mealList: List<Meal>,
    private val selectedDateinit: String,
    private val fragmentRecord: FragmentRecord
) : RecyclerView.Adapter<ExpandableAdapter.MyViewHolder>() {
    var selectedDate: String = selectedDateinit

    class MyViewHolder(
        val binding: ItemAccordionBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meal) {
            val txtName = binding.txtName
            val imgMore = binding.imgMore
            val layoutExpand = binding.layoutExpand

            txtName.text = meal.name

//            imgMore.setOnClickListener {
//                Log.d("mobileapp", "imgMore클릭")
//                // 1
//                val show = toggleLayout(!meal.isExpanded, it, layoutExpand)
//                meal.isExpanded = show
//            }
        }

//        fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: LinearLayout): Boolean {
//            // 2
//            ToggleAnimation.toggleArrow(view, isExpanded)
//            if (isExpanded) {
//                Log.d("mobileapp", "expand")
//                ToggleAnimation.expand(layoutExpand)
//            } else {
//                ToggleAnimation.collapse(layoutExpand)
//            }
//            return isExpanded
//        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemAccordionBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: LinearLayout): Boolean {
            // 2
            ToggleAnimation.toggleArrow(view, isExpanded)
            if (isExpanded) {
                Log.d("mobileapp", "expand")
                ToggleAnimation.expand(layoutExpand)
            } else {
                ToggleAnimation.collapse(layoutExpand)
            }
            return isExpanded
        }

        holder.bind(mealList[position])

        holder.binding.imgMore.setOnClickListener {

            Log.d("mobileapp", "imgMore클릭")
            // 1
            val show = toggleLayout(!mealList[position].isExpanded, it, holder.binding.layoutExpand)
            mealList[position].isExpanded = show

            Log.d("mobileapp", "${selectedDate}")
            Log.d("mobileapp", "${mealList[position].name}")
            if (MyApplication.checkAuth()) {
                MyApplication.db.collection("meals")
//                    .orderBy("date_time", Query.Direction.DESCENDING)
                    .whereEqualTo("checked_meal", mealList[position].name)
                    .whereEqualTo("date_time", selectedDate)
                    .get()
                    .addOnSuccessListener { result ->
                        val itemList = mutableListOf<FoodData>()
                        for (document in result) {
                            val item = document.toObject(FoodData::class.java)
                            item.docId = document.id
                            itemList.add(item)
                        }
                        holder.binding.ateFoodList.layoutManager = LinearLayoutManager(holder.binding.root.context)
                        holder.binding.ateFoodList.adapter = AteFoodAdapter(holder.binding.root.context, itemList)
                    }
                    .addOnFailureListener {
                        Toast.makeText(fragmentRecord.requireContext(), "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}