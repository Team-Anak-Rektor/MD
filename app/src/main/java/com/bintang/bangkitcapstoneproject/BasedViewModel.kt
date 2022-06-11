package com.bintang.bangkitcapstoneproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import kotlinx.coroutines.launch

class BasedViewModel(private val pref: SessionPreferences) : ViewModel() {

    fun getLoginSession(): LiveData<Boolean> = pref.getLoginSession().asLiveData()

}