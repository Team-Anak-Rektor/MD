package com.bintang.bangkitcapstoneproject.ui.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bintang.bangkitcapstoneproject.di.Injection
import com.bintang.bangkitcapstoneproject.ui.BasedViewModel
import com.bintang.bangkitcapstoneproject.ui.auth.login.LoginViewModel
import com.bintang.bangkitcapstoneproject.ui.home.HomeViewModel
import com.bintang.bangkitcapstoneproject.ui.profile.ProfileViewModel
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences

class ViewModelFactory(private val pref: SessionPreferences?, private val context: Context) : ViewModelProvider.Factory  {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(Injection.provideRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref!!) as T
            }
            modelClass.isAssignableFrom(BasedViewModel::class.java) -> {
                BasedViewModel(pref!!) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(pref!!) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}