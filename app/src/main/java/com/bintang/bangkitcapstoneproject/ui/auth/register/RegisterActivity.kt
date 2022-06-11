package com.bintang.bangkitcapstoneproject.ui.auth.register

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
import androidx.lifecycle.ViewModelProvider
import com.bintang.bangkitcapstoneproject.databinding.ActivityRegisterBinding
import com.bintang.bangkitcapstoneproject.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    private var isNameValid = true
    private var isEmailValid = true
    private var isPasswordValid = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        layoutConfig()
        viewAnimation()
        viewAction()
    }

    private fun viewAction() {
        binding.apply {

            edtName.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edtlName.error = "Name is required"
                    isNameValid = false
                } else {
                    edtlName.isErrorEnabled = false
                    isNameValid = true
                }
            }

            edtEmail.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edtlEmail.error = "Email is required"
                    isEmailValid = false
                } else {
                    edtlEmail.isErrorEnabled = false
                    isEmailValid = true
                }
            }

            edtPassword.doOnTextChanged { text, _, _, _ ->
                when {
                    text!!.length in 1..5 -> {
                        edtlPassword.helperText = "*Minimum 6 characters required"
                        isPasswordValid = false
                    }
                    text.length in 6..10 -> {
                        edtlPassword.helperText = "Good Password"
                        isPasswordValid = true
                    }
                    text.length >= 11 -> {
                        edtlPassword.helperText = "Strong Password"
                        isPasswordValid = true
                    }
                    else -> {
                        edtlPassword.helperText = "*Minimum 6 characters required"
                        isPasswordValid = false
                    }
                }
            }

            btnSignup.setOnClickListener {
                signup()
            }
        }
    }

    private fun signup() {
        
        binding.apply {

            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.length < 3 || !email.contains("@")){
                isEmailValid = false
                edtlEmail.error = "Email is not valid"
            }

            if (name.isEmpty()) {
                isNameValid = false
                edtlName.error = "Name is required"
            }

            if (email.isEmpty()) {
                isEmailValid = false
                edtlEmail.error = "Email is required"
            }

            if (password.isEmpty()) {
                isPasswordValid = false
                edtlPassword.error = "Password Required"
            }

            if (isNameValid && isEmailValid && isPasswordValid) {

                loadingLayout.loading.visibility = View.VISIBLE

                viewModel.register(name, email, password)

                viewModel.getRegisterResult().observe(this@RegisterActivity) {
                    loadingLayout.loading.visibility = View.INVISIBLE
                    if (it.status == "failed") {
                        val showDialog = AlertDialog
                            .Builder(this@RegisterActivity)
                            .setTitle("Registration failed, ${it.message}")
                        showDialog.setPositiveButton("Back") { dialog, _ ->
                            dialog.cancel()
                            finish()
                            startActivity(intent)
                        }
                        showDialog.show()

                    } else {
                        val showDialog = AlertDialog
                            .Builder(this@RegisterActivity)
                            .setTitle("Success, ${it.message}")
                        showDialog.setPositiveButton("Login") {
                                dialog, _ ->
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            dialog.dismiss()
                            finish()
                        }
                        showDialog.setNegativeButton("Back") {
                                dialog, _ -> dialog.cancel()
                        }
                        showDialog.show()
                        clearData()
                    }
                }
            }
        }
    }

    private fun clearData() {
        
        binding.apply {
            edtName.setText("")
            edtEmail.setText("")
            edtPassword.setText("")
            edtlName.isErrorEnabled = false
            edtlEmail.isErrorEnabled = false
            edtlPassword.isErrorEnabled = false
            edtName.clearFocus()
            edtEmail.clearFocus()
            edtPassword.clearFocus()
        }
    }

    private fun viewAnimation() {

        binding.apply {
            ObjectAnimator.ofFloat(imgCover, View.TRANSLATION_Y, -30f, 30f).apply {
                duration = 4000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val cvImage = ObjectAnimator.ofFloat(imgCover, View.ALPHA, 1f).setDuration(500)
            val tvTitle = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).apply {
                duration = 500
                startDelay = 100
            }
            val tvMessage = ObjectAnimator.ofFloat(tvSubtitle, View.ALPHA, 1f).apply {
                duration = 500
                startDelay = 100
            }
            val edtlName = ObjectAnimator.ofFloat(edtlName, View.ALPHA, 1f).setDuration(500)
            val edtlEmail = ObjectAnimator.ofFloat(edtlEmail, View.ALPHA, 1f).setDuration(500)
            val edtlPassword = ObjectAnimator.ofFloat(edtlPassword, View.ALPHA, 1f).setDuration(500)
            val edtName = ObjectAnimator.ofFloat(edtName, View.ALPHA, 1f).setDuration(500)
            val edtEmail = ObjectAnimator.ofFloat(edtEmail, View.ALPHA, 1f).setDuration(500)
            val edtPassword = ObjectAnimator.ofFloat(edtPassword, View.ALPHA, 1f).setDuration(500)
            val btnSignUp = ObjectAnimator.ofFloat(btnSignup, View.ALPHA, 1f).apply {
                duration = 500
                startDelay = 100
            }

            val titleAnimation = AnimatorSet().apply {
                playTogether(tvTitle, tvMessage)
            }

            val formAnimation = AnimatorSet().apply {
                playTogether(edtlName, edtName, edtlEmail, edtEmail, edtlPassword ,edtPassword)
            }

            AnimatorSet().apply {
                playSequentially(cvImage, titleAnimation, formAnimation, btnSignUp)
                start()
            }
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