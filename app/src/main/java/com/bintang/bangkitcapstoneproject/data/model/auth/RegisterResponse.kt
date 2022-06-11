package com.bintang.bangkitcapstoneproject.data.model.auth

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("requestAt")
    val requestAt: String,

    @field:SerializedName("message")
    val message: String
)
