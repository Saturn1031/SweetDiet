package com.narae.sweetdiet

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
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
        val checkedMeal = intent.getStringExtra("checkedMeal")!!
        val selectedDate = intent.getStringExtra("selectedDate")!!

        binding.txtMain.text = "${checkedMeal} 추가하기"

        searchFood = binding.edtFood.text.toString()
        binding.btnSearch.setOnClickListener {
            searchFood = binding.edtFood.text.toString()

            // http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1?ServiceKey=n6EBRN24jG%2BUrUXH%2FsU8SlHMyu1RBlJZvoO5woqXnoa0poCpn%2BiLZX0D3RXUafvYhhFLUa%2FB%2Fr7n3ZJSWDGuuQ%3D%3D&desc_kor=초코&numOfRows=3&pageNo=1&type=json
            val call: Call<String> = RetrofitConnection.jsonNetworkService.getJsonList(
                "n6EBRN24jG+UrUXH/sU8SlHMyu1RBlJZvoO5woqXnoa0poCpn+iLZX0D3RXUafvYhhFLUa/B/r7n3ZJSWDGuuQ==",
                searchFood,
                20,
                1,
                "json"
            )

            call?.enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Log.d("mobileapp", "$response")
                        Log.d("mobileapp", "${response.body()}")

                        val responseJson = Gson().fromJson(response.body(), JsonResponse::class.java)

                        binding.jsonRecyclerView.adapter = JsonAdapter(responseJson.body.items, checkedMeal, selectedDate, this@AddMealActivity)
                        binding.jsonRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("mobileapp", "공공 데이터 획득 실패")
                    Log.d("mobileapp", "$t")
                }
            })
        }
    }
}