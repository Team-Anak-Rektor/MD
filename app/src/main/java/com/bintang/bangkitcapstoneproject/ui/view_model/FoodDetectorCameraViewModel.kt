package com.bintang.bangkitcapstoneproject.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bintang.bangkitcapstoneproject.domain.repository.FoodDetectorCameraRepository
import com.bintang.bangkitcapstoneproject.ui.state_holders.FoodDetectorCameraUiState
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform

class FoodDetectorCameraViewModel(private val repository: FoodDetectorCameraRepository) : ViewModel() {
    val foodDetectorCameraUiState: StateFlow<FoodDetectorCameraUiState> = repository.foodDetectorCameraState.transform {
        emit(FoodDetectorCameraUiState(cameraType = it.Camera))
    }.stateIn(
        scope = viewModelScope,
        started = Lazily,
        initialValue = FoodDetectorCameraUiState(cameraType = "camera")
    )

    fun switchCameraOCR(){
        repository.switchCamera()
    }
}

@Suppress("UNCHECKED_CAST")
class FoodDetectorCameraViewModelFactory(private val repository: FoodDetectorCameraRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FoodDetectorCameraViewModel(repository) as T
    }
}