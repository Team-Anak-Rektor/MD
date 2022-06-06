package com.bintang.bangkitcapstoneproject.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.bangkitcapstoneproject.BasedActivity
import com.bintang.bangkitcapstoneproject.databinding.FragmentHomeBinding
import com.bintang.bangkitcapstoneproject.model.NearbySearchResult
import com.bintang.bangkitcapstoneproject.ui.restaurant_detail.RestaurantDetailActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var userLocation: String? = null
    private var initData: Boolean? = null
    
    
    private val coordinates = mutableListOf<String>()
    private val distance = mutableListOf<String>()
    private var asal = ""

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
        //viewAction()
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {

        //CHECKING LOCATION PERMISSION
        if (hasLocationPermission()) {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

            val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
            val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

            //CHECKING IF GPS IS ON
            task.addOnSuccessListener(requireActivity()) {
                binding.root.visibility = View.VISIBLE
                //GET GPS LOCATION DATA
                val lastLoc = fusedLocationProviderClient.lastLocation
                lastLoc.addOnSuccessListener(requireActivity()) {
                    if (it != null) {
                        //SET LIST RESTAURANT DATA
                        userLocation = "${it.latitude},${it.longitude}"
                        viewModel.setRestaurantList(userLocation!!)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Looking for your location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            //CHECKING IF GPS IS OFF
            task.addOnFailureListener(requireActivity()) { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        //SHOW DIALOG MESSAGE TO TURNING ON GPS AUTOMATICALLY
                        exception.startResolutionForResult(requireActivity(), 1)
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }

        } else {
            requestLocationPermission()
        }
    }

    private fun viewAction() {
        binding.apply {
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    searchView.clearFocus()
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (!query.isNullOrEmpty() && !query.isNullOrBlank()) {
                        viewModel.setRestaurantList(userLocation!!, query)
                    } else {
//                        viewModel.setRestaurantList(userLocation!!)
                    }
                    return false
                }
            })
        }
    }

    private fun getRestaurantList() {

        initData = viewModel.isInitData

        if (initData == true) {
            viewModel.setIsInitUser(false)
            initData = viewModel.isInitData
        }

        viewModel.getRestaurantList().observe(viewLifecycleOwner) { restaurants ->
            if (!restaurants.isNullOrEmpty()) {
                binding.loadingLayout.loading.visibility = View.INVISIBLE

//                for (e in restaurants) {
//                    val loc = "${e.geometry.location.lat},${e.geometry.location.lng}"
//                    coordinates.add(loc)
//                }
//
//                for (e1 in coordinates) {
//                    var asal0: String?
//                    viewModel.setDistance(e1, defaultLocation)
//                    viewModel.getDistance().observe(viewLifecycleOwner) {
////                        val distance0 = listOf(it.distance.text)
//                        asal = it.distance.text
//                        distance.add(asal)
//                        //Log.d("distanceee", asal)
//                        Log.d("distanceee", distance.toString())
//                    }
//                }
//                Log.d("coordinatesnn", "Resto: $coordinates")


                val layoutAdapter = RestaurantListAdapter(restaurants)
                binding.rvRestaurantItem.adapter = layoutAdapter

                layoutAdapter.setOnItemClickCallback(object :
                    RestaurantListAdapter.OnItemClickCallback {
                    override fun onItemClicked(item: NearbySearchResult) {
                        val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
                        intent.putExtra(RestaurantDetailActivity.EXTRA_PLACE_ID, item.placeId)
                        intent.putExtra(
                            RestaurantDetailActivity.EXTRA_USER_LOCATION,
                            userLocation
                        )
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
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
    }

}