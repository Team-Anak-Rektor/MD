package com.bintang.bangkitcapstoneproject.network.adhaar

import com.bintang.bangkitcapstoneproject.model.auth.LoginResponse
import com.bintang.bangkitcapstoneproject.model.auth.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}