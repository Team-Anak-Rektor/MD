package com.bintang.bangkitcapstoneproject.data.model.auth

import com.google.gson.annotations.SerializedName

data class UserData(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String
)
