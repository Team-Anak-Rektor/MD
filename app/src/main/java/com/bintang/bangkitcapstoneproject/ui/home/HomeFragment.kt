package com.bintang.bangkitcapstoneproject.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bintang.bangkitcapstoneproject.databinding.FragmentHomeBinding
import com.bintang.bangkitcapstoneproject.data.model.restaurant.NearbySearchResult
import com.bintang.bangkitcapstoneproject.ui.restaurant_detail.RestaurantDetailActivity
import com.bintang.bangkitcapstoneproject.ui.utils.ViewModelFactory
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: HomeViewModel
    private val adapter = RestaurantListAdapter()

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
            ViewModelFactory(pref = null, context = requireContext())
        )[HomeViewModel::class.java]

        //ITEM VISIBILITY
        binding.root.visibility = View.GONE
        binding.loadingLayout.loading.visibility = View.VISIBLE

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }

    //FRAGMENT LIFECYCLE
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvRestaurantItem.layoutManager = layoutManager

        setupUserLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> {
                binding.root.visibility = View.VISIBLE
                Handler().postDelayed({
                    getUserLocation()
                }, 3000)
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
            Manifest.permission.ACCESS_FINE_LOCATION
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

    private fun setupUserLocation() {

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

            task.addOnSuccessListener(requireActivity()) {
                binding.root.visibility = View.VISIBLE
                getUserLocation()
            }

            task.addOnFailureListener(requireActivity()) { e ->
                if (e is ResolvableApiException) {
                    try {
                        startIntentSenderForResult(
                            e.resolution.intentSender,
                            REQUEST_CHECK_SETTINGS,
                            null,
                            0,
                            0,
                            0,
                            null
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }
                }
            }

        } else {
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val lastLoc = fusedLocationClient.lastLocation
        lastLoc.addOnSuccessListener(requireActivity()) {
            if (it != null) {
                val userLocation = "${it.latitude},${it.longitude}"
                //getRestaurantList(userLocation)
            }
        }
    }

    private fun getRestaurantList(loc: String) {
        viewModel.getListOfRestaurant(loc = loc).observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(requireContext(), "Data not found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.submitData(lifecycle, it)
                binding.rvRestaurantItem.adapter = adapter
                adapter.setOnItemClickCallback(object :
                    RestaurantListAdapter.OnItemClickCallback {
                    override fun onItemClicked(item: NearbySearchResult) {
                        val intent = Intent(requireContext(), RestaurantDetailActivity::class.java)
                        intent.putExtra(RestaurantDetailActivity.EXTRA_PLACE_ID, item.placeId)
                        intent.putExtra(
                            RestaurantDetailActivity.EXTRA_USER_LOCATION,
                            loc
                        )
                        startActivity(intent)
                    }
                })
            }
        }
        binding.loadingLayout.loading.visibility = View.INVISIBLE
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
                        // viewModel.setRestaurantList(userLocation!!, query)
                    } else {
//                        viewModel.setRestaurantList(userLocation!!)
                    }
                    return false
                }
            })
        }
    }

    companion object {
        const val PERMISSION_LOCATION_REQUEST_CODE = 1
        const val REQUEST_CHECK_SETTINGS = 100
    }

}