package com.bintang.bangkitcapstoneproject.data.impl

import com.bintang.bangkitcapstoneproject.data.utils.FoodDetectorUiResource
import com.bintang.bangkitcapstoneproject.domain.repository.FoodDetectorRepository
import com.bintang.bangkitcapstoneproject.model.food.FoodDetectorResponse
import com.bintang.bangkitcapstoneproject.model.food.Result
import com.bintang.bangkitcapstoneproject.network.adhaar.AdhaarApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodDetectorRepositoryImpl : FoodDetectorRepository {
    override val foodDetectorUiState: MutableStateFlow<FoodDetectorUiResource> = MutableStateFlow(FoodDetectorUiResource(Loading = false))

    override val foodDetectorResultUiState: MutableStateFlow<Result> = MutableStateFlow(Result("", listOf(), listOf()))

    override suspend fun searchFood(label: String) {
        foodDetectorUiState.value = FoodDetectorUiResource(Loading = true)
        val client = AdhaarApiConfig.getApiService().getFood("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MiwiZW1haWwiOiJzdWhyaWFyQGdtYWlsLmNvbSIsImlhdCI6MTY1NDc1MTY1OX0.iDWByYZURXcO-8JKg3UDd4CgCmYN5iiyaM-nyPUyqYo", label)
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