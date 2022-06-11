package com.bintang.bangkitcapstoneproject.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bintang.bangkitcapstoneproject.domain.repository.RestaurantRepository
import com.bintang.bangkitcapstoneproject.data.model.restaurant.DistanceMatrixResponse
import com.bintang.bangkitcapstoneproject.data.model.restaurant.ElementsItem
import com.bintang.bangkitcapstoneproject.data.model.restaurant.NearbySearchResult
import com.bintang.bangkitcapstoneproject.data.network.google.GooglePlaceApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val restaurantRepository: RestaurantRepository) : ViewModel() {
    private val distance = MutableLiveData<ElementsItem>()
    var isInitData = true

    fun setIsInitUser(e: Boolean) {
        isInitData = e
    }

    fun getListOfRestaurant(keyword: String = "vegetarian", loc: String)
    : LiveData<PagingData<NearbySearchResult>> {
        return restaurantRepository.getNearbyRestaurant(keyword, loc).cachedIn(viewModelScope)
    }
}

