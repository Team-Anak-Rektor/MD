package com.bintang.bangkitcapstoneproject.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bintang.bangkitcapstoneproject.BasedActivity
import com.bintang.bangkitcapstoneproject.databinding.ActivityLoginBinding
import com.bintang.bangkitcapstoneproject.ui.auth.register.RegisterActivity
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import com.bintang.bangkitcapstoneproject.utils.ViewModelFactory

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var isEmailValid = true
    private var isPasswordValid = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SessionPreferences.getInstance(dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, this)
        )[LoginViewModel::class.java]

        layoutConfig()
        viewAnimation()
        viewAction()
    }

    private fun viewAction() {

        binding.apply {

            edtEmail.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edtlEmail.error = "Email must not be empty"
                    isEmailValid = false
                } else {
                    edtlEmail.isErrorEnabled = false
                    isEmailValid = true
                }
            }

            edtPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edtlPassword.error = "Password must not be empty"
                    isPasswordValid = false
                } else {
                    edtlPassword.isErrorEnabled = false
                    isPasswordValid = true
                }
            }

            btnLogin.setOnClickListener {

                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if (email.length < 3 || !email.contains("@")){
                    isPasswordValid = false
                    edtlEmail.error = "Email is not valid"
                }

                if (email.isEmpty()) {
                    isEmailValid = false
                    edtlEmail.error = "Email must not be empty"
                }

                if (password.isEmpty()) {
                    isPasswordValid = false
                    edtlPassword.error = "Password must not be empty"
                }

                if (isEmailValid && isPasswordValid){

                    loadingLayout.loading.visibility = View.VISIBLE

                    viewModel.login(email, password)

                    viewModel.getLoginResult().observe(this@LoginActivity){
                        loadingLayout.loading.visibility = View.INVISIBLE
                        if (it.status == "failed") {
                            AlertDialog
                                .Builder(this@LoginActivity)
                                .setTitle("Login Failed")
                                .setPositiveButton("Back") {
                                        dialog, _ -> dialog.dismiss()
                                    finish()
                                    startActivity(intent)
                                }
                                .show()

                        } else {
                            val intent = Intent(this@LoginActivity, BasedActivity::class.java)
                            startActivity(intent)
                            finish()
                            storeData(
                                true,
                                it.token,
                                it.userData!!.id.toString(),
                                it.userData.name,
                                it.userData.email
                            )
                        }
                    }
                }
            }

            btnSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun storeData(session: Boolean, token: String, id: String, name: String, email: String) {
        viewModel.setLoginSession(session)
        viewModel.setPrivateKey(token)
        viewModel.setUserId(id)
        viewModel.setUserName(name)
        viewModel.setUserEmail(email)
    }

    private fun viewAnimation() {

        val cvImage = ObjectAnimator.ofFloat(binding.imgCover, View.ALPHA, 1f).setDuration(500)
        val tvTitle = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).apply {
            duration = 500
            startDelay = 1300
        }
        val tvMessage = ObjectAnimator.ofFloat(binding.tvSubtitle, View.ALPHA, 1f).apply {
            duration = 500
            startDelay = 1300
        }
        val edtlEmail = ObjectAnimator.ofFloat(binding.edtlEmail, View.ALPHA, 1f).setDuration(500)
        val edtlPassword =
            ObjectAnimator.ofFloat(binding.edtlPassword, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtPassword =
            ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).apply {
            duration = 500
            startDelay = 500
        }
        val btnSignUp = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).apply {
            duration = 500
            startDelay = 500
        }

        val titleAnimation = AnimatorSet().apply {
            playTogether(tvTitle, tvMessage)
        }

        val formAnimation = AnimatorSet().apply {
            playTogether(edtlEmail, edtEmail, edtlPassword, edtPassword)
        }

        val btnAnimation = AnimatorSet().apply {
            playTogether(btnLogin, btnSignUp)
        }

        AnimatorSet().apply {
            playSequentially(cvImage, titleAnimation, formAnimation, btnAnimation)
            start()
        }
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