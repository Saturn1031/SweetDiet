package com.narae.sweetdiet

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

// http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1?ServiceKey=n6EBRN24jG%2BUrUXH%2FsU8SlHMyu1RBlJZvoO5woqXnoa0poCpn%2BiLZX0D3RXUafvYhhFLUa%2FB%2Fr7n3ZJSWDGuuQ%3D%3D&desc_kor=초코&numOfRows=3&pageNo=1&type=json
// https://apis.data.go.kr/1390802/AgriFood/FdFoodCkryImage/getKoreanFoodFdFoodCkryImageList?serviceKey=n6EBRN24jG%2BUrUXH%2FsU8SlHMyu1RBlJZvoO5woqXnoa0poCpn%2BiLZX0D3RXUafvYhhFLUa%2FB%2Fr7n3ZJSWDGuuQ%3D%3D&service_Type=json&Page_No=1&Page_Size=20&food_Name=%EB%B0%A5&ckry_Name=%EC%A1%B0%EB%A6%AC

class RetrofitConnection {
    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/"

        val jsonNetworkService: NetworkService

        val jsonRetrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        // Recipe
        private const val RECIPE_BASE_URL = "https://apis.data.go.kr/1390802/AgriFood/FdFoodCkryImage/"

        val recipeNetworkService: NetworkService

        val recipeRetrofit: Retrofit
            get() = Retrofit.Builder()
                .baseUrl(RECIPE_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        init {
            jsonNetworkService = jsonRetrofit.create(NetworkService::class.java) // base url과 쿼리 연결
            recipeNetworkService = recipeRetrofit.create(NetworkService::class.java)
        }
    }
}