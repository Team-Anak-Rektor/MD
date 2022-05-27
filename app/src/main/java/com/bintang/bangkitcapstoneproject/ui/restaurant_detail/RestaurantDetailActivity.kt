package com.bintang.bangkitcapstoneproject.ui.restaurant_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import com.bintang.bangkitcapstoneproject.databinding.ActivityRestaurantDetailBinding
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding
    private val images = mutableListOf<CarouselItem>()
    private val imgUrl = listOf(
        "https://cdn.pixabay.com/photo/2014/12/15/14/05/salad-569156_1280.jpg",
        "https://cdn.pixabay.com/photo/2016/11/18/14/05/brick-wall-1834784_1280.jpg",
        "https://cdn.pixabay.com/photo/2017/01/24/03/54/urban-2004494_1280.jpg",
        "https://cdn.pixabay.com/photo/2015/05/31/11/23/table-791167__480.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //StatusBar & NavBar Color Config
        layoutConfig()

        showRestaurantImage()
    }

    private fun showRestaurantImage() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.restaurantInfo.apply {
            carousel.registerLifecycle(lifecycle)

            for (e in imgUrl) {
                images.add(CarouselItem(imageUrl = e))
            }

            carousel.setData(images)
            carousel.setIndicator(index)
        }
    }

    private fun layoutConfig() {
        val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetController?.isAppearanceLightStatusBars = true
        windowInsetController?.isAppearanceLightNavigationBars = true
    }

}

