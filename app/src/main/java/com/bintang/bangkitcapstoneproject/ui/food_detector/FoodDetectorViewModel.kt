package com.bintang.bangkitcapstoneproject.ui.food_detector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoodDetectorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Food Detector Page"
    }
    val text: LiveData<String> = _text
}