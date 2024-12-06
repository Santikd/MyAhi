package com.dicoding.myahi.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.myahi.data.preferences.UserData
import com.dicoding.myahi.databinding.ActivityLoginBinding
import com.dicoding.myahi.view.ViewModelFactory
import com.dicoding.myahi.view.home.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        loginUser()
        playAnimation()
    }
    private fun loginUser() {
        viewModel.isLoading.observe(this){
            showLoading(it)
        }
        viewModel.isSuccess.observe(this) {
            showAlert(it)
        }
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            lifecycleScope.launch {
                viewModel.login(email, password)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showAlert(isSuccess: Boolean) {
        viewModel.messages.observe(this){ response ->
            val email = binding.emailEditText.text.toString()
            viewModel.token.observe(this) {
                viewModel.saveSession(UserData(email, it.toString()))
            }
            AlertDialog.Builder(this).apply {
                if (isSuccess) {
                    setTitle("Login Berhasil")
                    setMessage(response)
                    setPositiveButton("Lanjut") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                } else if (!isSuccess) {
                    setTitle("Login Gagal")
                    setMessage(response)
                    setNegativeButton("Kembali", null)
                }
                create()
                show()
            }
        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -50F, 50F).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.imageView, View.ROTATION, 0F, 10F, -10F, 0F).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        // Animasi fade-in dengan sedikit skala untuk elemen lainnya
        val titleTextView = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.titleTextView, View.SCALE_X, 0.8f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.titleTextView, View.SCALE_Y, 0.8f, 1f).setDuration(700)
            )
        }

        val messageTextView = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.messageTextView, View.TRANSLATION_Y, 50f, 0f).setDuration(700)
            )
        }

        val loginButton = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.loginButton, View.SCALE_X, 0.8f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.loginButton, View.SCALE_Y, 0.8f, 1f).setDuration(700)
            )
        }

        val emailTextView = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.emailTextView, View.TRANSLATION_X, -50f, 0f).setDuration(700)
            )
        }

        val emailEditTextLayout = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.TRANSLATION_X, 50f, 0f).setDuration(700)
            )
        }

        val passwordTextView = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.passwordTextView, View.TRANSLATION_X, -50f, 0f).setDuration(700)
            )
        }

        val passwordEditTextLayout = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(700),
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.TRANSLATION_X, 50f, 0f).setDuration(700)
            )
        }

        // Main AnimatorSet: Play animations sequentially with some overlap
        AnimatorSet().apply {
            playSequentially(
                titleTextView,
                messageTextView,
                AnimatorSet().apply { playTogether(emailTextView, emailEditTextLayout) },
                AnimatorSet().apply { playTogether(passwordTextView, passwordEditTextLayout) },
                loginButton
            )
            start()
        }
    }

}
