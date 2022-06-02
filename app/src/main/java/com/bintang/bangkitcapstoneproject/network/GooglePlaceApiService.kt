package com.bintang.bangkitcapstoneproject.network

import com.bintang.bangkitcapstoneproject.model.NearbySearchResponse
import retrofit2.http.GET
import retrofit2.http.Query
import com.bintang.bangkitcapstoneproject.BuildConfig
import com.bintang.bangkitcapstoneproject.model.DistanceMatrixResponse
import com.bintang.bangkitcapstoneproject.model.RestaurantDetailResponse
import retrofit2.Call


interface GooglePlaceApiService {

    @GET("place/nearbysearch/json")
    fun getNearbyRestaurant(
        @Query("keyword") keyword: String = "vegetarian",
        @Query("location") location: String,
        @Query("radius") radius: Int = 10000,
        @Query("type") type: String = "restaurant",
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
    ): Call<NearbySearchResponse>

    @GET("place/details/json")
    fun getRestaurantDetails(
        @Query("place_id") place_id: String,
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
    ): Call<RestaurantDetailResponse>

    @GET("distancematrix/json")
    fun getDistance(
        @Query("destinations") destinations: String,
        @Query("origins") origins: String,
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
    ): Call<DistanceMatrixResponse>


    //ALSUM : -6.2210,107.0019
    //Plaza Indonesia : -6.1939,106.8222
    //PALO ALTO : 37.4308,-122.1458
}