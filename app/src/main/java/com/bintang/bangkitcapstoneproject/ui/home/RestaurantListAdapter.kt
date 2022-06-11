package com.bintang.bangkitcapstoneproject.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bintang.bangkitcapstoneproject.BuildConfig
import com.bintang.bangkitcapstoneproject.R
import com.bintang.bangkitcapstoneproject.databinding.ItemRestaurantBinding
import com.bintang.bangkitcapstoneproject.data.model.restaurant.NearbySearchResult
import com.bumptech.glide.Glide

class RestaurantListAdapter : PagingDataAdapter<NearbySearchResult, RestaurantListAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class ListViewHolder(val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(restaurantList: NearbySearchResult) {
            binding.apply {

                //CHECKING PHOTO AVAILABILITY
                if (restaurantList.photos != null) {
                    val photoReference = restaurantList.photos[0].photoReference
                    val imgUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=225&photo_reference=$photoReference&key=${BuildConfig.MAPS_API_KEY}"
                    Glide.with(itemView.context)
                        .load(imgUrl)
                        .placeholder(R.drawable.img_place_holder)
                        .error(R.drawable.img_not_found)
                        .into(imgRestaurant)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.img_not_found)
                        .into(imgRestaurant)
                }

                //CHECKING OPENING STATUS AVAILABILITY
                if (restaurantList.openingHours == null) {
                    tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.red))
                    tvOpenStatus.text = "Closed"
                } else {
                    if (restaurantList.openingHours.openNow) {
                        tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.my_green))
                        tvOpenStatus.text = "Open Now"
                    } else {
                        tvOpenStatus.setTextColor(ContextCompat.getColor(tvOpenStatus.context, R.color.red))
                        tvOpenStatus.text = "Closed"
                    }
                }

                tvRestaurantName.text = restaurantList.name
                tvRating.text = restaurantList.rating.toString()
                //tvOthersInfo.text = restaurantDistance[i]
                //tvOthersInfo.text = "${restaurantDistance[i]} • ${priceLevelConverter(restaurantList[i].priceLevel)}"
            }

            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(restaurantList)
                //Log.d("tapp", "Item tapped")
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, i: Int) {
        val data = getItem(i)
        if (data != null) {
            holder.bind(data)
        } else {
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(item: NearbySearchResult)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NearbySearchResult>() {
            override fun areItemsTheSame(
                oldItem: NearbySearchResult,
                newItem: NearbySearchResult
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: NearbySearchResult,
                newItem: NearbySearchResult
            ): Boolean {
                return oldItem.placeId == newItem.placeId
            }
        }
    }
}