package com.narae.sweetdiet

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.narae.sweetdiet.databinding.FragmentRecordBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRecord.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRecord : Fragment() {
    lateinit var binding: FragmentRecordBinding

    // 달력
    val dateList = arrayListOf<Date>()
    val calendarAdapter = CalendarAdapter(dateList)
    lateinit var calendarList: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    var selectedDate = dateFormat.format(calendar.time)

    // 아코디언
    private lateinit var mealList: List<Meal>
    private lateinit var expandableAdapter: ExpandableAdapter

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecordBinding.inflate(inflater, container, false)

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy년 M월", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)

        binding.monthYearTv.text = currentDate

        calendarList = binding.calendarList
        mLayoutManager = LinearLayoutManager(context)

        // recyclerView orientation (가로 방향 스크롤 설정)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        calendarList.layoutManager = mLayoutManager

        setListView()

        // 아코디언
        val recyclerView = binding.recyclerList

        mealList = ArrayList()
        mealList = loadData()

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        expandableAdapter = ExpandableAdapter(mealList, selectedDate, this)
        recyclerView.adapter = expandableAdapter

        calendarAdapter.setItemClickListener(object: CalendarAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성

                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyyMM", Locale.getDefault())
                selectedDate = dateFormat.format(calendar.time) + dateList[position].date

                expandableAdapter.selectedDate = selectedDate
            }
        })

        // 식사 추가하기
        val items = arrayOf<String>("아침", "점심", "저녁", "간식")
        // 선택된 아이템의 인덱스를 저장할 변수
        var selected = 0

        val eventHandler = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val intent = Intent(requireContext(), AddMealActivity::class.java)

                if (which == DialogInterface.BUTTON_POSITIVE) {
                    Log.d("mobileapp", "BUTTON_POSITIVE")
                    if (items[selected].equals("아침")) {
                        intent.putExtra("checkedMeal", "아침")
                    } else if (items[selected].equals("점심")) {
                        intent.putExtra("checkedMeal", "점심")
                    } else if (items[selected].equals("저녁")) {
                        intent.putExtra("checkedMeal", "저녁")
                    }   else if (items[selected].equals("간식")) {
                        intent.putExtra("checkedMeal", "간식")
                    }
                    intent.putExtra("selectedDate", selectedDate)
                    startActivity(intent)
                } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                    Log.d("mobileapp", "BUTTON_NEGATIVE")
                }
            }
        }

        binding.btnAddMeal.setOnClickListener {
            // 하나만 선택 가능한 알림창 만들기
            AlertDialog.Builder(context).run() {
                setTitle("식사 종류 선택")
//                setIcon(android.R.drawable.ic_dialog_alert)

                // setSingleChoiceItems: 선택될 아이템들 설정 (아이템 배열, 기본 체크될 인덱스, 아이템 클릭 리스너)
                setSingleChoiceItems(items,0, object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        selected = which
                    }
                })
                setPositiveButton("예", eventHandler)
                setNegativeButton("아니오", eventHandler)
                show()
            }
        }

        return binding.root
    }

    // list(날짜, 요일)를 만들고, adapter를 등록하는 메소드
    private fun setListView() {
        // 현재 달의 마지막 날짜
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)

        val lastDayOfMonth = cal.getMaximum(Calendar.DAY_OF_MONTH)

        for(i: Int in 1..lastDayOfMonth) {
            cal.set(year, month, i)
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

            val dayOfWeekString = SimpleDateFormat("EEE", Locale.KOREA).format(cal.time)
            dateList.add(Date(dayOfWeekString, i.toString()))
        }
        calendarList.adapter = calendarAdapter
    }

    // 아코디언
    private fun loadData(): List<Meal> {
        val meals = ArrayList<Meal>()

        val mealStrings = resources.getStringArray(R.array.meal)

        for (i in mealStrings.indices) {
            val meal = Meal().apply {
                name = mealStrings[i]
            }
            meals.add(meal)
        }
        return meals
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentRecord.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRecord().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}