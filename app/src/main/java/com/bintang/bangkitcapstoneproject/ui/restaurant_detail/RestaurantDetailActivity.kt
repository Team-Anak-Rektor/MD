package com.bintang.bangkitcapstoneproject.ui.restaurant_detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.bintang.bangkitcapstoneproject.BuildConfig
import com.bintang.bangkitcapstoneproject.R
import com.bintang.bangkitcapstoneproject.databinding.ActivityRestaurantDetailBinding
import com.bintang.bangkitcapstoneproject.ui.view_model.RestaurantDetailViewModel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantDetailBinding
    private lateinit var viewModel: RestaurantDetailViewModel
    private lateinit var placeId: String
    private lateinit var userLocation: String
    private lateinit var restaurantUrl: String
    private lateinit var phoneNumber: String
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

        viewAction()
    }

    private fun showItem() {

        //SET RESTAURANT DATA
        viewModel.setPlaceId(placeId)

        //GET RESTAURANT DATA
        viewModel.getData().observe(this) {

            val destination = "${it.geometry.location.lat},${it.geometry.location.lng}"
            viewModel.setDistance(destination, userLocation)
            viewModel.getDistance().observe(this) { data ->
                binding.tvDistance.text = data.distance.text
            }

            //HIDING LOADING LAYOUT FROM SCREEN
            binding.loadingLayout.loading.visibility = View.INVISIBLE

            //INITIALIZE VARIABLE VALUE
            val restaurantName = it.name
            val location = it.addressComponents[5].longName
            val address = it.formattedAddress

            val openStatus = if (it.openingHours == null) {
                "Closed"
            } else {
                if (it.openingHours.openNow) {
                    "Open Now"
                } else {
                    "Closed"
                }
            }

            val openSchedule = if (it.openingHours == null) {
                "The restaurant is temporarily closed"
            } else {
                it.openingHours.weekdayText.toString()
                    .removePrefix("[")
                    .removeSuffix("]")
                    .replace(", ", "\n")
            }

            val ratings = if (it.rating == null) {
                "No rating yet"
            } else if (it.userRatingsTotal == null) {
                "${it.rating} (0)"
            } else {
                "${it.rating} (${it.userRatingsTotal})"
            }

            val distance = "0.0 km"

            val price = if (it.priceLevel == null) {
                "Price unknown"
            } else {
                when (it.priceLevel) {
                    1 -> "Inexpensive"
                    2 -> "Moderate"
                    3 -> "Expensive"
                    4 -> "Very Expensive"
                    else -> "Price unknown"
                }
            }

            //APPLY VIEW WITH DATA
            binding.apply {

                tvRestaurantName.text = restaurantName
                tvLocation.text = location
                tvRating.text = ratings
                tvDistance.text = distance
                tvPrice.text = price

                //SET OPEN STATUS
                when (openStatus) {
                    "Open Now" -> {
                        tvOpenStatus.apply {
                            text = "Open Now"
                            setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.my_green))
                        }
                    }
                    else -> {
                        tvOpenStatus.apply {
                            text = "Closed"
                            setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.red))
                        }
                    }
                }

                restaurantInfo.apply {

                    tvAddress.text = address
                    tvOpenSchedule.text = openSchedule

                    //SET IMAGE CAROUSEL
                    carousel.registerLifecycle(lifecycle)
                    if (it.photos != null) {
                        for (e in it.photos) {
                            val imgUrl =
                                "https://maps.googleapis.com/maps/api/place/photo?maxwidth=225&photo_reference=${e.photoReference}&key=${BuildConfig.MAPS_API_KEY}"
                            images.add(CarouselItem(imageUrl = imgUrl))
                        }
                        carousel.setData(images)
                        carousel.setIndicator(index)

                    } else {
                        images.add(CarouselItem(imageDrawable = R.drawable.img_not_found_wide))
                        carousel.setData(images)
                        carousel.setIndicator(index)
                    }

                    //SET TAGS
                    var i = 0
                    for (e in it.types) {
                        i += 1
                        if (i <= 3) {
                            val tvTag = TextView(this@RestaurantDetailActivity)
                            val params = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).also { it.setMargins(0, 0, 30, 0) }
                            tvTag.layoutParams = params
                            tvTag.text = "#$e"
                            tvTag.background = ContextCompat.getDrawable(this@RestaurantDetailActivity, R.drawable.bg_button_stroke)
                            tvTag.setTextColor(ContextCompat.getColor(this@RestaurantDetailActivity, R.color.my_green))
                            tvTag.setPadding(30,17,30,17)
                            tagsContainer.addView(tvTag)
                        }
                    }

                }

                //FETCH RESTAURANT URL & PHONE NUMBER FOR VIEW ACTION
                if (it.formattedPhoneNumber == null) {
                    btnCall.visibility = View.GONE
                } else {
                    phoneNumber = it.formattedPhoneNumber
                }

                if (it.url == null) {
                    btnNavigate.visibility = View.GONE
                    btnShare.visibility = View.GONE
                } else {
                    restaurantUrl = it.url
                }

            }
        }
    }

    private fun viewAction() {

        binding.apply {

            //BUTTON BACK ACTION
            btnBack.setOnClickListener {
                finish()
            }

            //BUTTON CALL ACTION, CHECKING PARAMS AVAILABILITY FIRST
            btnCall.setOnClickListener {
                val dialPhoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
                startActivity(dialPhoneIntent)
            }

            //BUTTON NAVIGATE ACTION, CHECKING PARAMS AVAILABILITY FIRST
            btnNavigate.setOnClickListener {
                val mapIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(restaurantUrl)
                )
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }

            //BUTTON SHARE ACTION, CHECKING PARAMS AVAILABILITY FIRST
            btnShare.setOnClickListener {
                val shareIntent = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, restaurantUrl)
                }, null)
                startActivity(shareIntent)
            }
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

