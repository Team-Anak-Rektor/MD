package com.bintang.bangkitcapstoneproject.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bintang.bangkitcapstoneproject.BuildConfig
import com.bintang.bangkitcapstoneproject.R
import com.bintang.bangkitcapstoneproject.databinding.ItemRestaurantBinding
import com.bintang.bangkitcapstoneproject.model.NearbySearchResult
import com.bumptech.glide.Glide

class RestaurantListAdapter(private val restaurantList: List<NearbySearchResult>) :
    RecyclerView.Adapter<RestaurantListAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class ListViewHolder(val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, i: Int) {
        holder.binding.apply {

            //CHECKING PHOTO AVAILABILITY
            if (restaurantList[i].photos != null) {
                val photoReference = restaurantList[i].photos[0].photoReference
                val imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=225&photo_reference=$photoReference&key=${BuildConfig.MAPS_API_KEY}"
                Glide.with(holder.itemView.context)
                    .load(imgUrl)
                    .placeholder(R.drawable.img_place_holder)
                    .error(R.drawable.img_not_found)
                    .into(imgRestaurant)
            } else {
                Glide.with(holder.itemView.context)
                    .load(R.drawable.img_not_found)
                    .into(imgRestaurant)
            }

            //CHECKING OPENING STATUS AVAILABILITY
            if (restaurantList[i].openingHours == null) {
                tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.red))
                tvOpenStatus.text = "Closed"
            } else {
                if (restaurantList[i].openingHours.openNow) {
                    tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.my_green))
                    tvOpenStatus.text = "Open Now"
                } else {
                    tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.red))
                    tvOpenStatus.text = "Closed"
                }
            }

            tvRestaurantName.text = restaurantList[i].name
            tvRating.text = restaurantList[i].rating.toString()
            tvOthersInfo.text = "3.0 km • ${priceLevelConverter(restaurantList[i].priceLevel)}"
        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(restaurantList[holder.adapterPosition])
        }
    }
    
    override fun getItemCount(): Int = restaurantList.size

    private fun priceLevelConverter(price: Int): String {
        return when (price) {
            1 -> ""
            2 -> "$$"
            3 -> "$$$"
            4 -> "$$$$"
            else -> ""
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    
    interface OnItemClickCallback {
        fun onItemClicked(item: NearbySearchResult)
    }
}