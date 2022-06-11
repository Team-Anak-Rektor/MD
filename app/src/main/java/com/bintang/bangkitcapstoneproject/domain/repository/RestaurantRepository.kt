package com.bintang.bangkitcapstoneproject.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bintang.bangkitcapstoneproject.data.model.restaurant.NearbySearchResult
import com.bintang.bangkitcapstoneproject.data.network.google.GooglePlaceApiService
import com.bintang.bangkitcapstoneproject.data.paging_source.RestaurantPagingSource

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