package com.bintang.bangkitcapstoneproject.data.impl

import com.bintang.bangkitcapstoneproject.domain.repository.FoodDetectorCameraRepository
import com.bintang.bangkitcapstoneproject.utils.FoodDetectorCameraResource
import kotlinx.coroutines.flow.MutableStateFlow

class FoodDetectorCameraRepositoryImpl : FoodDetectorCameraRepository {
    override val foodDetectorCameraState: MutableStateFlow<FoodDetectorCameraResource> = MutableStateFlow(
        FoodDetectorCameraResource(Camera = "camera")
    )

    override fun switchCamera() {
        foodDetectorCameraState.value = FoodDetectorCameraResource(Camera = "ocr")
    }
}