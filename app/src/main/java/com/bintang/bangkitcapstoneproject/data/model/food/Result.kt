package com.bintang.bangkitcapstoneproject.data.model.food

data class Result(
    val foodName: String,
    val ingredients: List<Ingredient>,
    val suitableFor: List<SuitableFor>
)