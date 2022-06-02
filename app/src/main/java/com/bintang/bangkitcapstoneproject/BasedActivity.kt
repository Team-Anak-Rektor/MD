package com.bintang.bangkitcapstoneproject

import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bintang.bangkitcapstoneproject.databinding.ActivityBasedBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class BasedActivity : AppCompatActivity() {

    //SHA256:GzYjOQmJtOyRpEbZlZYAoh/49bScljVcsr+2krzouH4 naufanirfanda24@gmail.com

    private lateinit var binding: ActivityBasedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBasedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutConfig()
        navigation()
    }

    private fun navigation() {
        //Bottom Navigation Config
        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList = null
        val navController = findNavController(R.id.nav_host_fragment_activity_based)
        navView.setupWithNavController(navController)
    }

    private fun layoutConfig() {
        //Disable Night Mode Layout
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        //StatusBar & NavBar Color Config
        val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetController?.isAppearanceLightStatusBars = true
        windowInsetController?.isAppearanceLightNavigationBars = true
    }


}