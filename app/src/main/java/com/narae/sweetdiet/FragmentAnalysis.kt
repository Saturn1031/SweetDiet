package com.narae.sweetdiet

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.narae.sweetdiet.databinding.FragmentAnalysisBinding
import java.io.BufferedReader
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.pow


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentAnalysis.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentAnalysis : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentAnalysisBinding
    lateinit var itemList: MutableList<FoodData>
    lateinit var today: String
    lateinit var mpPieChart: PieChart
    lateinit var barChart: HorizontalBarChart

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
        binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        itemList = mutableListOf<FoodData>()
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        today = dateFormat.format(System.currentTimeMillis())
        MyApplication.db.collection("meals")
            .whereEqualTo("date_time", today)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val item = document.toObject(FoodData::class.java)
                    item.docId = document.id
                    itemList.add(item)
                }
                makePieChart(itemList)
                makeBarChart(itemList)
            }
            .addOnFailureListener {
                Toast.makeText(context, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
            }

        binding.refresh.setOnClickListener {
            itemList.clear()
            MyApplication.db.collection("meals")
                .whereEqualTo("date_time", today)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val item = document.toObject(FoodData::class.java)
                        item.docId = document.id
                        itemList.add(item)
                    }
                    makePieChart(itemList)
                    makeBarChart(itemList)
                }
                .addOnFailureListener {
                    Toast.makeText(context, "서버 데이터 획득 실패", Toast.LENGTH_SHORT).show()
                }
        }

        return binding.root
    }

    fun makePieChart(itemList: MutableList<FoodData>) {
        // 파일 읽기 (최초 실행 시에는 myBodyInfoFile.txt가 없으므로 주석처리 후 실행해야 함)
        val file = File(context?.filesDir, "myBodyInfoFile.txt")
        val readstream : BufferedReader = file.reader().buffered()
        val bodyInfo = readstream.readLine()
        val height = bodyInfo.split("_")[0]
        val weight = bodyInfo.split("_")[1]
        val gender = bodyInfo.split("_")[2]

        // 열량 권장 섭취량
        var recommendCalories = 0f
        if (gender.equals("남자")) {
            recommendCalories = ((height.toFloat()/100).pow(2) * 22) * 30
        } else {
            recommendCalories = ((height.toFloat()/100).pow(2) * 21) * 30
        }

        // 열량 섭취량
        var intakeCalories = 0f
        if (itemList.isEmpty()) {
            intakeCalories = 0f
        } else {
            for (item in itemList) {
                if (item.calorie.equals("N/A")) {
                    continue
                }
                intakeCalories += item.calorie?.toFloat() ?: 0f
            }
        }

        mpPieChart = binding.MPpieChart

        // 그래프에 나타낼 데이터
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(intakeCalories, "현재 섭취량"))
        entries.add(PieEntry(recommendCalories - intakeCalories, "남은 섭취량"))

        // 그래프 색상(데이터 순서)
        val colors = listOf(
            getResources().getColor(R.color.green, null),
            Color.parseColor("#D2D1D4")
        )

        // 데이터, 색상, 글자크기 및 색상 설정
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 12F
        dataSet.valueTextColor = R.color.black

        // Pie 그래프 생성
        val dataMPchart = PieData(dataSet)
        mpPieChart.data = dataMPchart
        // 중앙 텍스트를 설정하여 섭취량 표시
        mpPieChart.centerText =
            String.format("열량 (kcal)\n%.0f / %.0f", intakeCalories, recommendCalories)
        mpPieChart.setCenterTextSize(16f)
        // 범례와 그래프 설명 비활성화
        mpPieChart.legend.isEnabled = false
        mpPieChart.description.isEnabled = false
        // 그래프 업데이트
        mpPieChart.invalidate()
    }

    fun makeBarChart(itemList: MutableList<FoodData>) {
        // 파일 읽기 (최초 실행 시에는 myBodyInfoFile.txt가 없으므로 주석처리 후 실행해야 함)
        val file = File(context?.filesDir, "myBodyInfoFile.txt")
        val readstream : BufferedReader = file.reader().buffered()
        val bodyInfo = readstream.readLine()
        val height = bodyInfo.split("_")[0]
        val weight = bodyInfo.split("_")[1]
        val gender = bodyInfo.split("_")[2]
        // 열량 권장 섭취량
        var recommendCalories = 0f
        if (gender.equals("남자")) {
            recommendCalories = ((height.toFloat()/100).pow(2) * 22) * 30
        } else {
            recommendCalories = ((height.toFloat()/100).pow(2) * 21) * 30
        }

        val recommendCarbohydrate = recommendCalories * 0.55f / 4
        val recommendProtein = recommendCalories * 0.20f / 4
        val recommendFat = recommendCalories * 0.25f / 9
        val recommendSugar = recommendCalories * 0.1f / 4
        val recommendSoduim = 2000f
        val recommnedCholesterol = 200f
        val recommnedSaturatedFat = recommendCalories * 0.07f / 9
        val recommendTransFat = recommendCalories * 0.01f / 9

        var intakeCarbohydrate = 0f
        if (itemList.isEmpty()) {
            intakeCarbohydrate = 0f
        } else {
            for (item in itemList) {
                if (item.carbohydrate.equals("N/A")) {
                    continue
                }
                intakeCarbohydrate += item.carbohydrate?.toFloat() ?: 0f
            }
        }
        var intakeProtein = 0f
        if (itemList.isEmpty()) {
            intakeProtein = 0f
        } else {
            for (item in itemList) {
                if (item.protein.equals("N/A")) {
                    continue
                }
                intakeProtein += item.protein?.toFloat() ?: 0f
            }
        }
        var intakeFat = 0f
        if (itemList.isEmpty()) {
            intakeFat = 0f
        } else {
            for (item in itemList) {
                if (item.fat.equals("N/A")) {
                    continue
                }
                intakeFat += item.fat?.toFloat() ?: 0f
            }
        }
        var intakeSugar = 0f
        if (itemList.isEmpty()) {
            intakeSugar = 0f
        } else {
            for (item in itemList) {
                if (item.sugar.equals("N/A")) {
                    continue
                }
                intakeSugar += item.sugar?.toFloat() ?: 0f
            }
        }
        var intakeSoduim = 0f
        if (itemList.isEmpty()) {
            intakeSoduim = 0f
        } else {
            for (item in itemList) {
                if (item.sodium.equals("N/A")) {
                    continue
                }
                intakeSoduim += item.sodium?.toFloat() ?: 0f
            }
        }
        var intakeCholesterol = 0f
        if (itemList.isEmpty()) {
            intakeCholesterol = 0f
        } else {
            for (item in itemList) {
                if (item.cholesterol.equals("N/A")) {
                    continue
                }
                intakeCholesterol += item.cholesterol?.toFloat() ?: 0f
            }
        }
        var intakeSaturatedFat = 0f
        if (itemList.isEmpty()) {
            intakeSaturatedFat = 0f
        } else {
            for (item in itemList) {
                if (item.saturated_fat.equals("N/A")) {
                    continue
                }
                intakeSaturatedFat += item.saturated_fat?.toFloat() ?: 0f
            }
        }
        var intakeTransFat = 0f
        if (itemList.isEmpty()) {
            intakeTransFat = 0f
        } else {
            for (item in itemList) {
                if (item.trans_fat.equals("N/A")) {
                    continue
                }
                intakeTransFat += item.trans_fat?.toFloat() ?: 0f
            }
        }

        var width = 0

        val carbohydrateParams = binding.graphCarbohydrate.layoutParams
        width = (intakeCarbohydrate / recommendCarbohydrate * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            carbohydrateParams.width = binding.maxBar.width
        } else {
            carbohydrateParams.width = width
        }
        binding.graphCarbohydrate.layoutParams = carbohydrateParams
        binding.txtCarbohydrate.text = String.format("%.0f / %.0f", intakeCarbohydrate, recommendCarbohydrate)

        val proteinParams = binding.graphProtein.layoutParams
        width = (intakeProtein / recommendProtein * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            proteinParams.width = binding.maxBar.width
        } else {
            proteinParams.width = width
        }
        binding.graphProtein.layoutParams = proteinParams
        binding.txtProtein.text = String.format("%.0f / %.0f", intakeProtein, recommendProtein)

        val fatParams = binding.graphFat.layoutParams
        width = (intakeFat / recommendFat * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            fatParams.width = binding.maxBar.width
        } else {
            fatParams.width = width
        }
        binding.graphFat.layoutParams = fatParams
        binding.txtFat.text = String.format("%.0f / %.0f", intakeFat, recommendFat)

        val sugarParams = binding.graphSugar.layoutParams
        width = (intakeSugar / recommendSugar * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            sugarParams.width = binding.maxBar.width
        } else {
            sugarParams.width = width
        }
        binding.graphSugar.layoutParams = sugarParams
        binding.txtSugar.text = String.format("%.0f / %.0f", intakeSugar, recommendSugar)

        val sodiumParams = binding.graphSodium.layoutParams
        width = (intakeSoduim / recommendSoduim * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            sodiumParams.width = binding.maxBar.width
        } else {
            sodiumParams.width = width
        }
        binding.graphSodium.layoutParams = sodiumParams
        binding.txtSodium.text = String.format("%.0f / %.0f", intakeSoduim, recommendSoduim)

        val cholesterolParams = binding.graphCholesterol.layoutParams
        width = (intakeCholesterol / recommnedCholesterol * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            cholesterolParams.width = binding.maxBar.width
        } else {
            cholesterolParams.width = width
        }
        binding.graphCholesterol.layoutParams = cholesterolParams
        binding.txtCholesterol.text = String.format("%.0f / %.0f", intakeCholesterol, recommnedCholesterol)

        val saturatedFatParams = binding.graphSaturatedFat.layoutParams
        width = (intakeSaturatedFat / recommnedSaturatedFat * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            saturatedFatParams.width = binding.maxBar.width
        } else {
            saturatedFatParams.width = width
        }
        Log.d("mobileapp", "saturatedFat width : ${saturatedFatParams.width}")
        binding.graphSaturatedFat.layoutParams = saturatedFatParams
        binding.txtSaturatedFat.text = String.format("%.0f / %.0f", intakeSaturatedFat, recommnedSaturatedFat)

        val transFatParams = binding.graphTransFat.layoutParams
        width = (intakeTransFat / recommendTransFat * binding.maxBar.width).toInt()
        if (width >= binding.maxBar.width) {
            transFatParams.width = binding.maxBar.width
        } else {
            transFatParams.width = width
        }
        binding.graphTransFat.layoutParams = transFatParams
        binding.txtTransFat.text = String.format("%.0f / %.0f", intakeTransFat, recommendTransFat)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentAnalysis.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentAnalysis().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}