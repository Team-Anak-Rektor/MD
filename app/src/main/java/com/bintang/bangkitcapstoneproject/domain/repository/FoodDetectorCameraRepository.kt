package com.bintang.bangkitcapstoneproject.domain.repository

import com.bintang.bangkitcapstoneproject.utils.FoodDetectorCameraResource
import kotlinx.coroutines.flow.MutableStateFlow

interface FoodDetectorCameraRepository {
    val foodDetectorCameraState: MutableStateFlow<FoodDetectorCameraResource>
    fun switchCamera()
}