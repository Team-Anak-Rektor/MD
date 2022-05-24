package com.bintang.bangkitcapstoneproject

import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bintang.bangkitcapstoneproject.databinding.ActivityBasedBinding

class BasedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBasedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBasedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Bottom Navigation Config
        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment_activity_based)
        navView.setupWithNavController(navController)

        //StatusBar Config
        val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetController?.isAppearanceLightStatusBars = true
    }
}