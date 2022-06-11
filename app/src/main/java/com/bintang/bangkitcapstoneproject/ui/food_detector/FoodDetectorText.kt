package com.bintang.bangkitcapstoneproject.ui.food_detector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bintang.bangkitcapstoneproject.data.impl.FoodDetectorRepositoryImpl
import com.bintang.bangkitcapstoneproject.databinding.ActivityFoodDetectorTextBinding
import kotlinx.coroutines.launch

class FoodDetectorText : AppCompatActivity() {
    private lateinit var binding: ActivityFoodDetectorTextBinding
    private val viewModel:FoodDetectorViewModel by viewModels{
        FoodDetectorViewModelFactory(
            FoodDetectorRepositoryImpl()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodDetectorTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        if (extras != null){
            val label = extras.getString("label") ?: "not found"
            viewModel.searchFood(label)
        } else {
            binding.landingLayout.visibility = View.VISIBLE
            binding.errorLayout.visibility = View.INVISIBLE
            binding.resultLayout.visibility = View.INVISIBLE
            binding.loadingLayout.loading.visibility = View.INVISIBLE
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.foodDetectorUIState.collect{
                    when {
                        it.Loading -> {
                            binding.loadingLayout.loading.visibility = View.VISIBLE
                            binding.resultLayout.visibility = View.INVISIBLE
                            binding.errorLayout.visibility = View.INVISIBLE
                            binding.landingLayout.visibility = View.GONE
                        }
                        it.Success -> {
                            binding.resultLayout.visibility = View.VISIBLE
                            binding.errorLayout.visibility = View.INVISIBLE
                            binding.loadingLayout.loading.visibility = View.INVISIBLE
                        }
                        it.Error != null -> {
                            binding.errorLayout.visibility = View.VISIBLE
                            binding.resultLayout.visibility = View.INVISIBLE
                            binding.loadingLayout.loading.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.foodDetectorResultUIState.collect{
                    val searchResultText = "Result : ${it.foodName}"
                    binding.searchResult.text = searchResultText

                    var ingredientResultText = ""
                    for (item in it.ingredients){
                        ingredientResultText += "${item.ingredientName}, "
                    }
                    binding.ingredient.text = ingredientResultText

                    var compatibilityResultText = ""
                    for (item in it.suitableFor){
                        compatibilityResultText += "${item.diet}, "
                    }
                    binding.consumer.text = compatibilityResultText
                }
            }
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.btnSearch.setOnClickListener{
            if (binding.searchBar.query.isEmpty()) {
                Toast.makeText(this@FoodDetectorText, "Silahkan input nama makanan terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.searchFood(binding.searchBar.query.toString())
            }
        }

    }
}