package com.bintang.bangkitcapstoneproject.ui.food_detector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bintang.bangkitcapstoneproject.databinding.ActivityFoodValidatorTextBinding

class FoodValidatorText : AppCompatActivity() {
    private lateinit var binding: ActivityFoodValidatorTextBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodValidatorTextBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}