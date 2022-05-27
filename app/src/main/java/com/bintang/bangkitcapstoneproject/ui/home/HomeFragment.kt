package com.bintang.bangkitcapstoneproject.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.bangkitcapstoneproject.databinding.FragmentHomeBinding
import com.bintang.bangkitcapstoneproject.model.Restaurant
import com.bintang.bangkitcapstoneproject.ui.restaurant_detail.RestaurantDetailActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val restaurantList = mutableListOf<Restaurant>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for (e in 1..20) {
            restaurantList.add(
                Restaurant(
                    "Salad Stop $e, Plaza Indonesia",
                    "4.$e",
                    "Halal • $e.0 km • $$$")
            )
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRestaurantItem.layoutManager = layoutManager

        val layoutAdapter = RestaurantListAdapter(restaurantList)
        binding.rvRestaurantItem.adapter = layoutAdapter

        layoutAdapter.setOnItemClickCallback(object : RestaurantListAdapter.OnItemClickCallback{
            override fun onItemClicked(item: Restaurant) {
                val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
                startActivity(intent)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
