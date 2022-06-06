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
import com.bintang.bangkitcapstoneproject.network.GooglePlaceApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {
    private val listRestaurant = MutableLiveData<List<NearbySearchResult>>()
    private val distance = MutableLiveData<ElementsItem>()
    var isInitData = true

    fun setIsInitUser(e: Boolean) {
        isInitData = e
    }

    fun setRestaurantList(loc: String, keyword: String = "vegetarian") {
        val client = GooglePlaceApiConfig.getApiService().getNearbyRestaurant(location = loc, keyword = keyword)
        client.enqueue(object : Callback<NearbySearchResponse>{
            override fun onResponse(
                call: Call<NearbySearchResponse>,
                response: Response<NearbySearchResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listRestaurant.postValue(responseBody.results)
                    }
                }
            }
            override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                Log.d("Failur : ", t.message.toString())
            }
        })
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

    fun getRestaurantList() : LiveData<List<NearbySearchResult>> = listRestaurant

    fun getDistance() : LiveData<ElementsItem> = distance
}

//class HomeViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel() {
//    private val listRestaurant = MutableLiveData<List<NearbySearchResult>>()
//    private val distance = MutableLiveData<ElementsItem>()
//    var isInitData = true
//
//    fun setIsInitUser(e: Boolean) {
//        isInitData = e
//    }
//
//    fun getRestaurantList(keyword: String, loc: String): LiveData<PagingData<NearbySearchResult>> {
//        return restaurantRepository.getNearbyRestaurant(keyword, loc).cachedIn(viewModelScope)
//    }
//
//    fun setDistance(destination: String, origin: String) {
//        val client = GooglePlaceApiConfig.getApiService().getDistance(destination, origin)
//        client.enqueue(object : Callback<DistanceMatrixResponse>{
//            override fun onResponse(
//                call: Call<DistanceMatrixResponse>,
//                response: Response<DistanceMatrixResponse>
//            ) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        distance.postValue(responseBody.rows[0].elements[0])
//                    }
//                }
//            }
//            override fun onFailure(call: Call<DistanceMatrixResponse>, t: Throwable) {
//                Log.d("Failur : ", t.message.toString())
//            }
//        })
//    }
//
//    fun getDistance() : LiveData<ElementsItem> = distance
//}