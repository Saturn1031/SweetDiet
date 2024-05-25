package com.narae.sweetdiet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.narae.sweetdiet.databinding.ActivityAddMealBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMealActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddMealBinding
    lateinit var searchFood: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent = intent
        val checked = intent.getStringExtra("checked")!!
        binding.txtMain.text = "${checked} 추가하기"

        searchFood = binding.edtFood.text.toString()
        binding.btnSearch.setOnClickListener {
            searchFood = binding.edtFood.text.toString()

            // http://apis.data.go.kr/1470000/FoodNtrIrdntInfoService/getFoodNtrItdntList?ServiceKey=n6EBRN24jG%2BUrUXH%2FsU8SlHMyu1RBlJZvoO5woqXnoa0poCpn%2BiLZX0D3RXUafvYhhFLUa%2FB%2Fr7n3ZJSWDGuuQ%3D%3D&desc_kor=초코&numOfRows=3&pageNo=1&type=json
            val call: Call<JsonResponse> = RetrofitConnection.jsonNetworkService.getJsonList(
                "n6EBRN24jG%2BUrUXH%2FsU8SlHMyu1RBlJZvoO5woqXnoa0poCpn%2BiLZX0D3RXUafvYhhFLUa%2FB%2Fr7n3ZJSWDGuuQ%3D%3D",
                searchFood,
                20,
                1,
                "json"
            )

            call?.enqueue(object: Callback<JsonResponse> {
                override fun onResponse(call: Call<JsonResponse>, response: Response<JsonResponse>) {
                    if (response.isSuccessful) {
                        Log.d("mobileapp", "$response")
                        Log.d("mobileapp", "${response.body()}")
                        binding.jsonRecyclerView.adapter = JsonAdapter(response.body()?.body!!.items)
                        binding.jsonRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
                    }
                }

                override fun onFailure(call: Call<JsonResponse>, t: Throwable) {
                    Log.d("mobileapp", "onFailure")
                    Log.d("mobileapp", "$t")
                }
            })
        }
    }
}