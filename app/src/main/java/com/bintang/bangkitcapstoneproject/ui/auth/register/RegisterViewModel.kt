package com.bintang.bangkitcapstoneproject.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintang.bangkitcapstoneproject.data.model.auth.RegisterResponse
import com.bintang.bangkitcapstoneproject.data.network.adhaar.AdhaarApiConfig
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val registerResult = MutableLiveData<RegisterResponse>()

    fun register(name: String, email: String, password: String) {
        val client = AdhaarApiConfig.getApiService().signup(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    if (responseBody.status != "failed") {
                        registerResult.postValue(responseBody)
                    }
                } else {
                    val messageResponse = JSONObject(
                        response.errorBody()!!.charStream().readText()
                    ).getString("message")
                    registerResult.postValue(RegisterResponse("failed", "", messageResponse))
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.d("eRegister", t.message.toString())
            }
        })
    }

    fun getRegisterResult() : LiveData<RegisterResponse> = registerResult

}