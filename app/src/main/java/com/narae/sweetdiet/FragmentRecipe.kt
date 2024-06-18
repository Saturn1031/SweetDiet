package com.narae.sweetdiet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.narae.sweetdiet.databinding.FragmentRecipeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRecipe.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRecipe : Fragment() {
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
        val binding = FragmentRecipeBinding.inflate(inflater, container, false)

        binding.btnSearch.setOnClickListener {
            val searchRecipe = binding.edtRecipe.text.toString()

            // https://apis.data.go.kr/1390802/AgriFood/FdFoodCkryImage/getKoreanFoodFdFoodCkryImageList?serviceKey=n6EBRN24jG%2BUrUXH%2FsU8SlHMyu1RBlJZvoO5woqXnoa0poCpn%2BiLZX0D3RXUafvYhhFLUa%2FB%2Fr7n3ZJSWDGuuQ%3D%3D&service_Type=json&Page_No=1&Page_Size=20&food_Name=%EB%B0%A5&ckry_Name=%EC%A1%B0%EB%A6%AC
            val call: Call<String> = RetrofitConnection.recipeNetworkService.getJsonListRecipe(
                "n6EBRN24jG+UrUXH/sU8SlHMyu1RBlJZvoO5woqXnoa0poCpn+iLZX0D3RXUafvYhhFLUa/B/r7n3ZJSWDGuuQ==",
                "json",
                1,
                20,
                searchRecipe,
                "조리"
            )

            call?.enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Log.d("mobileapp", "$response")
                        Log.d("mobileapp", "${response.body()}")

                        val newBody = response.body()?.replace(", \"food_List\"", "\"food_List\"")
                        val responseJson = Gson().fromJson(newBody, JsonResponseRecipe::class.java)

                        binding.jsonRecyclerView.adapter = JsonAdapterRecipe(binding.root.context, responseJson.response.list)
                        binding.jsonRecyclerView.layoutManager = LinearLayoutManager(binding.root.context)
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("mobileapp", "공공 데이터 획득 실패")
                    Log.d("mobileapp", "$t")
                }
            })
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentRecipe.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRecipe().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}