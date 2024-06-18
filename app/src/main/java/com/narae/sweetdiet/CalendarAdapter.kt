package com.narae.sweetdiet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.narae.sweetdiet.databinding.CalendarCellBinding
import java.util.Calendar

class CalendarAdapter(private val dataSet: ArrayList<Date>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    private var selectedPosition: Int = RecyclerView.NO_POSITION

    // 현재 날짜 가져오기
    private val calendar = Calendar.getInstance()
    private val todayDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    init {
        selectedPosition = todayDayOfMonth - 1
    }

    class ViewHolder(val binding: CalendarCellBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Date) {
            binding.dateCell.text = item.date
            binding.dayCell.text = item.day
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = CalendarCellBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])

        // 클릭한 아이템에만 배경 설정
        if (position == selectedPosition) {
            holder.binding.dateCell.setBackgroundResource(R.drawable.round)
        } else {
            // 다른 아이템들에는 배경을 제거
            holder.binding.dateCell.background = null
        }

        holder.binding.root.setOnClickListener {
            // 클릭한 아이템의 위치 추적
            val previousSelectedPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            // 이전에 선택한 아이템을 갱신
            notifyItemChanged(previousSelectedPosition)
            // 현재 선택한 아이템을 갱신
            notifyItemChanged(selectedPosition)

            itemClickListener.onClick(it, position)
        }
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    override fun getItemCount() = dataSet.size
}