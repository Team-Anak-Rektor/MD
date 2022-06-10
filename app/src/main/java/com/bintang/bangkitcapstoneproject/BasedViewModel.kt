package com.bintang.bangkitcapstoneproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import kotlinx.coroutines.launch

class BasedViewModel(private val pref: SessionPreferences) : ViewModel() {
    fun setLoginSession(isLoggedIn: Boolean) {
        viewModelScope.launch {
            pref.setLoginSession(isLoggedIn)
        }
    }

    fun setPrivateKey(privateKey: String) {
        viewModelScope.launch {
            pref.setPrivateKey(privateKey)
        }
    }

    fun getLoginSession(): LiveData<Boolean> = pref.getLoginSession().asLiveData()

    fun getPrivateKey(): LiveData<String> = pref.getPrivateKey().asLiveData()
}