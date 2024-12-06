
package com.dicoding.myahi.view.landing

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.myahi.databinding.ActivityLandingBinding
import com.dicoding.myahi.view.login.LoginActivity
import com.dicoding.myahi.view.register.RegisterActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActions()
        playAnimation()
        supportActionBar?.hide()
    }

    private fun setupActions() {
        binding.loginButton.setOnClickListener {
            navigateToLogin()
        }
        binding.signupButton.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
    private fun playAnimation() {

        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -50f, 50f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.imageView, View.ROTATION, 0f, 10f, -10f, 0f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.loginButton, View.SCALE_X, 0.8f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.loginButton, View.SCALE_Y, 0.8f, 1f).setDuration(500)
            )
        }

        val signup = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.signupButton, View.TRANSLATION_Y, 50f, 0f).setDuration(500)
            )
        }

        val title = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(600),
                ObjectAnimator.ofFloat(binding.titleTextView, View.TRANSLATION_Y, -30f, 0f).setDuration(600)
            )
        }

        val desc = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 0f, 1f).setDuration(600),
                ObjectAnimator.ofFloat(binding.descTextView, View.SCALE_X, 0.8f, 1f).setDuration(600),
                ObjectAnimator.ofFloat(binding.descTextView, View.SCALE_Y, 0.8f, 1f).setDuration(600)
            )
        }


        AnimatorSet().apply {
            playSequentially(title, desc, AnimatorSet().apply { playTogether(login, signup) })
            start()
        }
    }

}
