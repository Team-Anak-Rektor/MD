package com.bintang.bangkitcapstoneproject.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.bangkitcapstoneproject.databinding.FragmentHomeBinding
import com.bintang.bangkitcapstoneproject.model.NearbySearchResult
import com.bintang.bangkitcapstoneproject.ui.restaurant_detail.RestaurantDetailActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentUserLoc = "-6.1939,106.8222"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[HomeViewModel::class.java]

        //ITEM VISIBILITY
        binding.root.visibility = View.GONE
        binding.loadingLayout.loading.visibility = View.VISIBLE

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        getUserLocation()

        return binding.root
    }

    //FRAGMENT LIFECYCLE
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRestaurantItem.layoutManager = layoutManager

        getRestaurantList()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        if (hasLocationPermission()) {
            binding.root.visibility = View.VISIBLE
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                try {
                    if (it.latitude != null && it.longitude != null) {
                        Log.d("XOX", "${it.latitude}, ${it.longitude}")
                        currentUserLoc = "${it.latitude},${it.longitude}"
                        viewModel.setRestaurantList(currentUserLoc)
                    }
                } catch (e: Exception) {
                    Log.e("Exception Locx", e.message, e)
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun getRestaurantList() {
        viewModel.getRestaurantList().observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding.loadingLayout.loading.visibility = View.INVISIBLE

                val layoutAdapter = RestaurantListAdapter(it)
                binding.rvRestaurantItem.adapter = layoutAdapter

                layoutAdapter.setOnItemClickCallback(object :
                    RestaurantListAdapter.OnItemClickCallback {
                    override fun onItemClicked(item: NearbySearchResult) {
                        val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
                        intent.putExtra(RestaurantDetailActivity.EXTRA_PLACE_ID, item.placeId)
                        intent.putExtra(RestaurantDetailActivity.EXTRA_USER_LOCATION, currentUserLoc)
                        startActivity(intent)
                    }
                })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //PERMISSIONS
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            this
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {

        binding.root.visibility = View.VISIBLE

        Toast.makeText(
            requireContext(),
            "Location Granted",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "This application need to access your location",
            PERMISSION_LOCATION_REQUEST_CODE,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1

        //Bekasi Utara
        const val LAT = "-6.2212152"
        const val LON = "107.0025133"
    }

}

//        BuildConfig.MAPS_API_KEY

//        binding.btnSearch.setOnClickListener {
//            if (hasLocationPermission()) {
//                //binding.root.visibility = View.VISIBLE
//                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
//                    val geocoder = Geocoder(requireContext())
//                    val currentLocation = geocoder.getFromLocation(it.latitude, it.longitude, 1)
//                    //Log.d("LOCX", currentLocation.first().locality)
//                    Log.d("LOCX", "Lat: ${it.latitude} - Lon: ${it.longitude}")
//                }
//            } else {
//                requestLocationPermission()
//            }
//        }