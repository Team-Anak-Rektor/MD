package com.bintang.bangkitcapstoneproject.data.network.google

import retrofit2.http.GET
import retrofit2.http.Query
import com.bintang.bangkitcapstoneproject.BuildConfig
import com.bintang.bangkitcapstoneproject.data.model.restaurant.DistanceMatrixResponse
import com.bintang.bangkitcapstoneproject.data.model.restaurant.NearbySearchResponse
import com.bintang.bangkitcapstoneproject.data.model.restaurant.RestaurantDetailResponse
import retrofit2.Call

interface GooglePlaceApiService {

    //NEARBY SEARCH
    @GET("place/nearbysearch/getFood")
    suspend fun getNearbyRestaurant(
        @Query("keyword") keyword: String,
        @Query("location") location: String,
        @Query("radius") radius: Int = 10000,
        @Query("type") type: String = "restaurant",
        @Query("pagetoken") pageToken: String,
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
    ): NearbySearchResponse

    //PLACE DETAILS
    @GET("place/details/getFood")
    fun getRestaurantDetails(
        @Query("place_id") place_id: String,
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
    ): Call<RestaurantDetailResponse>

    //DISTANCE COUNTER
    @GET("distancematrix/getFood")
    fun getDistance(
        @Query("destinations") destinations: String,
        @Query("origins") origins: String,
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
    ): Call<DistanceMatrixResponse>
}