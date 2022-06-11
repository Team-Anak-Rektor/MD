package com.bintang.bangkitcapstoneproject.domain.repository

import com.bintang.bangkitcapstoneproject.data.utils.FoodDetectorUiResource
import com.bintang.bangkitcapstoneproject.model.food.FoodDetectorResponse
import com.bintang.bangkitcapstoneproject.model.food.Result
import kotlinx.coroutines.flow.MutableStateFlow

interface FoodDetectorRepository {
    val foodDetectorUiState: MutableStateFlow<FoodDetectorUiResource>
    val foodDetectorResultUiState: MutableStateFlow<Result>
    suspend fun searchFood(label: String)
}