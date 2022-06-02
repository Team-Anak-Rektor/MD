package com.bintang.bangkitcapstoneproject.ui.restaurant_detail

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.bintang.bangkitcapstoneproject.BuildConfig
import com.bintang.bangkitcapstoneproject.R
import com.bintang.bangkitcapstoneproject.databinding.ActivityRestaurantDetailBinding
import com.bintang.bangkitcapstoneproject.ui.view_model.RestaurantDetailViewModel
import com.bumptech.glide.Glide
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding
    private lateinit var viewModel: RestaurantDetailViewModel
    private lateinit var placeId: String
    private lateinit var userLocation: String
    private val images = mutableListOf<CarouselItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestaurantDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loadingLayout.loading.visibility = View.VISIBLE

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[RestaurantDetailViewModel::class.java]

        placeId = intent.getStringExtra(EXTRA_PLACE_ID).toString()
        userLocation = intent.getStringExtra(EXTRA_USER_LOCATION).toString()

        //StatusBar & NavBar Color Config
        layoutConfig()

        showItem()
    }

    private fun showItem() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        viewModel.setPlaceId(placeId)

        viewModel.getData().observe(this){
            binding.loadingLayout.loading.visibility = View.INVISIBLE

            binding.apply {

                if (it.openingHours == null) {
                    tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.red))
                    tvOpenStatus.text = "Closed"
                } else {
                    if (it.openingHours.openNow) {
                        tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.my_green))
                        tvOpenStatus.text = "Open Now"
                    } else {
                        tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.red))
                        tvOpenStatus.text = "Closed"
                    }
                }

                tvRestaurantName.text = it.name
                tvLocation.text = it.addressComponents[5].shortName
                tvRating.text = "${it.rating} (${it.userRatingsTotal})"
                tvPrice.text = priceLevelConverter(it.priceLevel)

                //IMAGE CAROUSEL
                restaurantInfo.apply {

                    carousel.registerLifecycle(lifecycle)

                    if (it.photos != null){
                        for (e in it.photos) {
                            val imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=225&photo_reference=${e.photoReference}&key=${BuildConfig.MAPS_API_KEY}"
                            images.add(CarouselItem(imageUrl = imgUrl))
                        }
                        carousel.setData(images)
                        carousel.setIndicator(index)

                    } else {
                        images.add(CarouselItem(imageDrawable = R.drawable.img_not_found_wide))
                        carousel.setData(images)
                        carousel.setIndicator(index)
                    }
                }
            }
        }
    }

    private fun priceLevelConverter(price: Int): String {
        return when (price) {
            1 -> "Inexpensive"
            2 -> "Moderate"
            3 -> "Expensive"
            4 -> "Very Expensive"
            else -> "unknown"
        }
    }

    private fun layoutConfig() {
        val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetController?.isAppearanceLightStatusBars = true
        windowInsetController?.isAppearanceLightNavigationBars = true
    }

    companion object {
        const val EXTRA_PLACE_ID = "PLACE_ID"
        const val EXTRA_USER_LOCATION = "USER_LOCATION"
    }

}

