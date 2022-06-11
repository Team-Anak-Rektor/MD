package com.bintang.bangkitcapstoneproject.model.food

data class FoodDetectorResponse(
    val requestAt: String,
    val result: Result,
    val status: String
)