package com.bintang.bangkitcapstoneproject.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bintang.bangkitcapstoneproject.model.auth.LoginResponse
import com.bintang.bangkitcapstoneproject.model.auth.UserData
import com.bintang.bangkitcapstoneproject.network.adhaar.AdhaarApiConfig
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: SessionPreferences) : ViewModel() {

    //LOGIN
    private val loginResult = MutableLiveData<LoginResponse>()

    fun login(email: String, password: String) {
        val client = AdhaarApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    if (responseBody.status != "failed") {
                        loginResult.postValue(responseBody)
                    }
                } else {
                    val messageResponse = JSONObject(
                        response.errorBody()!!.charStream().readText()
                    ).getString("message")
                    loginResult.postValue(LoginResponse("failed", "", messageResponse, ""))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("eLogin", t.message.toString())
            }

        })
    }

    fun getLoginResult() : LiveData<LoginResponse> = loginResult


    //SESSION PREFERENCES
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

    fun setUserId(id: String) {
        viewModelScope.launch {
            pref.setUserId(id)
        }
    }

    fun setUserName(name: String) {
        viewModelScope.launch {
            pref.setUserName(name)
        }
    }

    fun setUserEmail(email: String) {
        viewModelScope.launch {
            pref.setUserEmail(email)
        }
    }

}