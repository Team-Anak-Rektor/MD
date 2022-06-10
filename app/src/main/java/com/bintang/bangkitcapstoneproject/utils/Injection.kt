package com.bintang.bangkitcapstoneproject.utils

import android.content.Context
import com.bintang.bangkitcapstoneproject.domain.repository.RestaurantRepository
import com.bintang.bangkitcapstoneproject.network.google.GooglePlaceApiConfig

object Injection {
    fun provideRepository(context: Context) : RestaurantRepository {
        val apiService = GooglePlaceApiConfig.getApiService()
        return RestaurantRepository(apiService)
    }
}