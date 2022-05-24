package com.bintang.bangkitcapstoneproject.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintang.bangkitcapstoneproject.databinding.ItemRestaurantBinding
import com.bintang.bangkitcapstoneproject.model.Restaurant

class RestaurantListAdapter(private val restaurantList: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantListAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRestaurantBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            tvRestaurantName.text = restaurantList[position].restaurantName
            tvRating.text = restaurantList[position].rating
            tvOthersInfo.text = restaurantList[position].others
        }
    }

    override fun getItemCount(): Int = restaurantList.size
}