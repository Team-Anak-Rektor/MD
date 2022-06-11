package com.bintang.bangkitcapstoneproject.ui.state_holders

data class FoodDetectorUiState(
    val Loading: Boolean,
    val Success: Boolean,
    val Error: String? = null
)

data class FoodDetectorResultUiState(
    val foodName: String,
    val ingredients: List<IngredientUiState>,
    val suitableFor: List<CompatibilityUiState>
)

data class IngredientUiState(
    val ingredientName: String
)

data class CompatibilityUiState(
    val diet: String
)