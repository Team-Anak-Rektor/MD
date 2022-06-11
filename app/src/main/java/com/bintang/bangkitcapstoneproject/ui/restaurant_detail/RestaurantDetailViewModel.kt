package com.bintang.bangkitcapstoneproject.ui.restaurant_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintang.bangkitcapstoneproject.data.model.restaurant.DistanceMatrixResponse
import com.bintang.bangkitcapstoneproject.data.model.restaurant.ElementsItem
import com.bintang.bangkitcapstoneproject.data.model.restaurant.RestaurantDetailResponse
import com.bintang.bangkitcapstoneproject.data.model.restaurant.RestaurantDetailResult
import com.bintang.bangkitcapstoneproject.data.network.google.GooglePlaceApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantDetailViewModel: ViewModel() {
    private val data = MutableLiveData<RestaurantDetailResult>()
    private val distance = MutableLiveData<ElementsItem>()

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

    fun setDistance(destination: String, origin: String) {
        val client = GooglePlaceApiConfig.getApiService().getDistance(destination, origin)
        client.enqueue(object : Callback<DistanceMatrixResponse>{
            override fun onResponse(
                call: Call<DistanceMatrixResponse>,
                response: Response<DistanceMatrixResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        distance.postValue(responseBody.rows[0].elements[0])
                    }
                }
            }
            override fun onFailure(call: Call<DistanceMatrixResponse>, t: Throwable) {
                Log.d("Failur : ", t.message.toString())
            }
        })
    }

    fun getDistance() : LiveData<ElementsItem> = distance

}