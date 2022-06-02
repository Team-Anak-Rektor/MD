package com.bintang.bangkitcapstoneproject.ui.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintang.bangkitcapstoneproject.model.RestaurantDetailResponse
import com.bintang.bangkitcapstoneproject.model.RestaurantDetailResult
import com.bintang.bangkitcapstoneproject.network.GooglePlaceApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDetailViewModel: ViewModel() {
    private val data = MutableLiveData<RestaurantDetailResult>()


    fun setPlaceId(placeId: String) {
        val client = GooglePlaceApiConfig.getApiService().getRestaurantDetails(placeId)
        client.enqueue(object : Callback<RestaurantDetailResponse>{
            override fun onResponse(
                call: Call<RestaurantDetailResponse>,
                response: Response<RestaurantDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        data.postValue(responseBody.result)
                    }
                }
            }
            override fun onFailure(call: Call<RestaurantDetailResponse>, t: Throwable) {
                Log.d("Failur : ", t.message.toString())
            }
        })
    }

    fun getData() : LiveData<RestaurantDetailResult> = data

}