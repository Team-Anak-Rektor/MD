package com.bintang.bangkitcapstoneproject.data.impl

import com.bintang.bangkitcapstoneproject.data.utils.FoodDetectorUiResource
import com.bintang.bangkitcapstoneproject.domain.repository.FoodDetectorRepository
import com.bintang.bangkitcapstoneproject.model.food.FoodDetectorResponse
import com.bintang.bangkitcapstoneproject.model.food.Result
import com.bintang.bangkitcapstoneproject.network.adhaar.AdhaarApiConfig
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.take
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodDetectorRepositoryImpl(private val pref: SessionPreferences) : FoodDetectorRepository {
    override val foodDetectorUiState: MutableStateFlow<FoodDetectorUiResource> = MutableStateFlow(FoodDetectorUiResource(Loading = false))

    override val foodDetectorResultUiState: MutableStateFlow<Result> = MutableStateFlow(Result("", listOf(), listOf()))

    override suspend fun searchFood(label: String) {
        foodDetectorUiState.value = FoodDetectorUiResource(Loading = true)
        val token = pref.getPrivateKey().take(1).firstOrNull()
        val client = AdhaarApiConfig.getApiService().getFood("Bearer $token", label)
        client.enqueue(object : Callback<FoodDetectorResponse>{
            override fun onResponse(
                call: Call<FoodDetectorResponse>,
                response: Response<FoodDetectorResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        foodDetectorUiState.value = FoodDetectorUiResource(Loading = false, Success = true)
                        foodDetectorResultUiState.value = Result(foodName = responseBody.result.foodName, ingredients = responseBody.result.ingredients, suitableFor = responseBody.result.suitableFor)
                    } else {
                        foodDetectorUiState.value = FoodDetectorUiResource(Loading = false, Success = false, Error = response.message())
                    }
                } else {
                    foodDetectorUiState.value = FoodDetectorUiResource(Loading = false, Success = false, Error = response.message())
                }
            }

            override fun onFailure(call: Call<FoodDetectorResponse>, t: Throwable) {
                foodDetectorUiState.value = FoodDetectorUiResource(Loading = false, Success = false, Error = t.message)
            }
        })
    }

}