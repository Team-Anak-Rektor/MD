package com.bintang.bangkitcapstoneproject.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.bangkitcapstoneproject.databinding.FragmentHomeBinding
import com.bintang.bangkitcapstoneproject.model.Restaurant

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

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

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRestaurantItem.layoutManager = layoutManager

        val restaurantList = listOf<Restaurant>(
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
            Restaurant("Waroeng Sehat, Summarecon Beksi", "4.6", "Halal • 3.0 km • $$$"),
        )

        val layoutAdapter = RestaurantListAdapter(restaurantList)
        binding.rvRestaurantItem.adapter = layoutAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}