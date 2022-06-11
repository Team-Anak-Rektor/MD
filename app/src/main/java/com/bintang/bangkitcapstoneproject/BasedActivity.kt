package com.bintang.bangkitcapstoneproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bintang.bangkitcapstoneproject.databinding.ActivityBasedBinding
import com.bintang.bangkitcapstoneproject.model.auth.UserData
import com.bintang.bangkitcapstoneproject.ui.auth.login.LoginActivity
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import com.bintang.bangkitcapstoneproject.utils.ViewModelFactory
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class BasedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBasedBinding
    private lateinit var viewModel: BasedViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, this)
        )[BasedViewModel::class.java]

        layoutConfig()
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModel.getLoginSession().observe(this) {
            if (!it) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)
                finish()
            } else {
                navigation()
            }
        }
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

    companion object {
        const val EXTRA_USER_DATA = "Extra User Data"
    }

}