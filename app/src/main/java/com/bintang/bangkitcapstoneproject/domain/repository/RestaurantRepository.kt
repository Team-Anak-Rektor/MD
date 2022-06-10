package com.bintang.bangkitcapstoneproject.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bintang.bangkitcapstoneproject.model.restaurant.NearbySearchResult
import com.bintang.bangkitcapstoneproject.network.google.GooglePlaceApiService

class RestaurantRepository(private val apiService: GooglePlaceApiService) {

    fun getNearbyRestaurant(keyword: String, location: String)
    : LiveData<PagingData<NearbySearchResult>> {
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false),
            pagingSourceFactory = {
                RestaurantPagingSource(apiService, keyword, location)
            }
        ).liveData
    }
}