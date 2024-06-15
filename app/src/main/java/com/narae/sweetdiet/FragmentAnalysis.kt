package com.narae.sweetdiet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.narae.sweetdiet.databinding.FragmentAnalysisBinding
import java.io.BufferedReader
import java.io.File
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
        val binding = FragmentAnalysisBinding.inflate(inflater, container, false)

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

        val mpPieChart: PieChart = binding.MPpieChart

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
            String.format("열량 \n%.0f kcal \n/ %.0f kcal", intakeCalories, recommendCalories)
        mpPieChart.setCenterTextSize(16f)
        // 범례와 그래프 설명 비활성화
        mpPieChart.legend.isEnabled = false
        mpPieChart.description.isEnabled = false
        // 그래프 업데이트
        mpPieChart.invalidate()

        return binding.root
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