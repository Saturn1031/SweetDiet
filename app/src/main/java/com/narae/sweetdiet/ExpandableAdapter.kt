package com.narae.sweetdiet

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
            txtName.text = meal.name
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyViewHolder(ItemAccordionBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        fun toggleLayout(isExpanded: Boolean, view: View, layoutExpand: LinearLayout): Boolean {
            ToggleAnimation.toggleArrow(view, isExpanded)
            if (isExpanded) {
                ToggleAnimation.expand(layoutExpand)
            } else {
                ToggleAnimation.collapse(layoutExpand)
            }
            return isExpanded
        }

        holder.bind(mealList[position])

        holder.binding.imgMore.setOnClickListener {
            val show = toggleLayout(!mealList[position].isExpanded, it, holder.binding.layoutExpand)
            mealList[position].isExpanded = show

            Log.d("mobileapp", "${selectedDate}")
            Log.d("mobileapp", "${mealList[position].name}")
            if (MyApplication.checkAuth() || MyApplication.email != null) {
                MyApplication.db.collection("meals")
                    .whereEqualTo("email", MyApplication.email)
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
                        Log.d("mobileapp", "서버 데이터 획득 실패")
                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}