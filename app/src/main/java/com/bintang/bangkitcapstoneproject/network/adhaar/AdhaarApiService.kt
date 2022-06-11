package com.bintang.bangkitcapstoneproject.network.adhaar

import com.bintang.bangkitcapstoneproject.model.auth.LoginResponse
import com.bintang.bangkitcapstoneproject.model.auth.RegisterResponse
import com.bintang.bangkitcapstoneproject.model.food.FoodDetectorResponse
import retrofit2.Call
import retrofit2.http.*

interface AdhaarApiService {

    //SIGNUP
    @FormUrlEncoded
    @POST("auth/register")
    fun signup (
        @Field("fullName") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    //LOGIN
    @FormUrlEncoded
    @POST("auth/login")
    fun login (
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    //Food Detector
    @GET("foods/json")
    fun getFood (
        @Header("Authorization") token: String,
        @Query("label") label: String
    ): Call<FoodDetectorResponse>
}