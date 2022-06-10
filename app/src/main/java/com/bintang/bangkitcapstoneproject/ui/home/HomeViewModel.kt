package com.bintang.bangkitcapstoneproject.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bintang.bangkitcapstoneproject.domain.repository.RestaurantRepository
import com.bintang.bangkitcapstoneproject.model.*
import com.bintang.bangkitcapstoneproject.model.restaurant.DistanceMatrixResponse
import com.bintang.bangkitcapstoneproject.model.restaurant.ElementsItem
import com.bintang.bangkitcapstoneproject.model.restaurant.NearbySearchResult
import com.bintang.bangkitcapstoneproject.network.google.GooglePlaceApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel() {
    private val listRestaurant = MutableLiveData<List<NearbySearchResult>>()
    private val distance = MutableLiveData<ElementsItem>()
    var isInitData = true

    fun setIsInitUser(e: Boolean) {
        isInitData = e
    }

    fun getListOfRestaurant(keyword: String = "vegetarian", loc: String)
    : LiveData<PagingData<NearbySearchResult>> {
        return restaurantRepository.getNearbyRestaurant(keyword, loc).cachedIn(viewModelScope)
    }

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

