package com.narae.sweetdiet

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1?ServiceKey=n6EBRN24jG%2BUrUXH%2FsU8SlHMyu1RBlJZvoO5woqXnoa0poCpn%2BiLZX0D3RXUafvYhhFLUa%2FB%2Fr7n3ZJSWDGuuQ%3D%3D&desc_kor=초코&numOfRows=3&pageNo=1&type=json

interface NetworkService {
    @GET("getFoodNtrItdntList1")
    fun getJsonList(
        @Query("serviceKey") serviceKey: String,
        @Query("desc_kor") desc_kor: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("type") type: String,
    ): Call<String>

    @GET("getKoreanFoodFdFoodCkryImageList")
    fun getJsonListRecipe(
        @Query("serviceKey") serviceKey: String,
        @Query("service_Type") service_Type: String,
        @Query("Page_No") Page_No: Int,
        @Query("Page_Size") Page_Size: Int,
        @Query("food_Name") food_Name: String,
        @Query("ckry_Name") ckry_Name: String,
    ): Call<String>
}