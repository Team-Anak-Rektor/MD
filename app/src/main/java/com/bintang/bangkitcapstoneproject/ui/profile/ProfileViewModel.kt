package com.bintang.bangkitcapstoneproject.ui.profile

import androidx.lifecycle.*
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import kotlinx.coroutines.launch

class ProfileViewModel(private val pref: SessionPreferences) : ViewModel() {

    fun setPrivateKey(privateKey: String) {
        viewModelScope.launch {
            pref.setPrivateKey(privateKey)
        }
    }

    fun setLoginSession(isLoggedIn: Boolean) {
        viewModelScope.launch {
            pref.setLoginSession(isLoggedIn)
        }
    }

    fun setUserId(id: String) {
        viewModelScope.launch {
            pref.setUserId(id)
        }
    }

    fun getUserId() : LiveData<String> = pref.getUserId().asLiveData()

    fun setUserName(name: String) {
        viewModelScope.launch {
            pref.setUserName(name)
        }
    }

    fun getUserName() : LiveData<String> = pref.getUserName().asLiveData()

    fun setUserEmail(email: String) {
        viewModelScope.launch {
            pref.setUserEmail(email)
        }
    }

    fun getUserEmail() : LiveData<String> = pref.getUserEmail().asLiveData()

}