package com.bintang.bangkitcapstoneproject.ui.food_detector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bintang.bangkitcapstoneproject.domain.repository.FoodDetectorRepository
import com.bintang.bangkitcapstoneproject.ui.state_holders.CompatibilityUiState
import com.bintang.bangkitcapstoneproject.ui.state_holders.FoodDetectorResultUiState
import com.bintang.bangkitcapstoneproject.ui.state_holders.FoodDetectorUiState
import com.bintang.bangkitcapstoneproject.ui.state_holders.IngredientUiState
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class FoodDetectorViewModel(
    private val repository: FoodDetectorRepository
) : ViewModel() {

    val foodDetectorUIState: StateFlow<FoodDetectorUiState> =
        repository.foodDetectorUiState.transform {
            when {
                it.Loading -> emit(FoodDetectorUiState(Loading = it.Loading, Success = it.Success))
                it.Success -> emit(FoodDetectorUiState(Loading = it.Loading, Success = it.Success))
                it.Error != null -> emit(FoodDetectorUiState(Loading = it.Loading, Success = it.Loading, Error = it.Error))
            }
        }.stateIn(
            scope = viewModelScope,
            started = Lazily,
            initialValue = FoodDetectorUiState(Loading = false, Success = false)
        )

    val foodDetectorResultUIState: StateFlow<FoodDetectorResultUiState> = repository.foodDetectorResultUiState.transform {

        val listOfIngredient = mutableListOf<IngredientUiState>()
        for (item in it.ingredients){
            listOfIngredient.add(IngredientUiState(ingredientName = item.ingredientName))
        }

        val listOfCompatibleDiet = mutableListOf<CompatibilityUiState>()
        for (item in it.suitableFor){
            listOfCompatibleDiet.add(CompatibilityUiState(diet = item.diet))
        }

        val foodDetectorResultUi = FoodDetectorResultUiState(
            foodName = it.foodName,
            ingredients = listOfIngredient,
            suitableFor = listOfCompatibleDiet
        )
        emit(foodDetectorResultUi)

    }.stateIn(
        viewModelScope,
        started = Lazily,
        initialValue = FoodDetectorResultUiState(foodName = "", ingredients = listOf(), suitableFor = listOf())
    )

    fun searchFood(label: String){
        viewModelScope.launch {
            repository.searchFood(label)
        }
    }
}

class FoodDetectorViewModelFactory(private val repository: FoodDetectorRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodDetectorViewModel::class.java)){
            return FoodDetectorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow ViewModel class")
    }

}