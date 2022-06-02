package com.bintang.bangkitcapstoneproject.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import com.bintang.bangkitcapstoneproject.BasedActivity
import com.bintang.bangkitcapstoneproject.R
import com.bintang.bangkitcapstoneproject.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        layoutConfig()
        viewAnimation()
        viewAction()
    }

    private fun viewAction() {

        var isValid = true

        binding.apply {

            edtEmail.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edtlEmail.error = "Email must not be empty"
                } else {
                    edtlEmail.isErrorEnabled = false
                    isValid = true
                }
            }

            edtPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edtlPassword.error = "Password must not be empty"
                } else {
                    edtlPassword.isErrorEnabled = false
                    isValid = true
                }
            }

            btnLogin.setOnClickListener {

                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                if (email.length < 4 || !email.contains("@")) {
                    isValid = false
                    edtlEmail.error = "Email is not valid"
                }

                if (email.isEmpty()) {
                    isValid = false
                    edtlEmail.error = "Email must not be empty"
                }

                if (password.isEmpty()) {
                    isValid = false
                    edtlPassword.error = "Password must not be empty"
                }

                if (isValid) {
                    btnLogin.setOnClickListener {
                        val intent = Intent(this@LoginActivity, BasedActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            btnSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

        }
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
        val edtlPassword = ObjectAnimator.ofFloat(binding.edtlPassword, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtPassword = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
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
            playTogether(edtlEmail, edtEmail, edtlPassword ,edtPassword)
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