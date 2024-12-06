package com.dicoding.myahi.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.myahi.R
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.myahi.databinding.ActivityRegisterBinding
import com.dicoding.myahi.view.ViewModelFactory
import com.dicoding.myahi.view.landing.LandingActivity
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        registerUser()
        playAnimation()
    }

    private fun registerUser() {
        observeViewModel()

        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            lifecycleScope.launch {
                showLoading(true)
                viewModel.registerUser(name, email, password)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            showAlert(isSuccess)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showAlert(isSuccess: Boolean) {
        viewModel.message.observe(this) { response ->
            if (isSuccess) {

                val intent = Intent(this, LandingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {

                AlertDialog.Builder(this).apply {
                    setTitle(R.string.register_gagal)
                    setMessage(response)
                    setNegativeButton(R.string.kembali) { dialog, _ ->
                        dialog.cancel()
                    }
                    create()
                    show()
                }
            }
        }
    }
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -50F, 50F).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        ObjectAnimator.ofFloat(binding.imageView, View.ROTATION, 0F, 10F, -10F, 0F).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTextView = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 0f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.titleTextView, View.SCALE_X, 0.8f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.titleTextView, View.SCALE_Y, 0.8f, 1f).setDuration(500)
            )
        }

        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 0f, 1f).setDuration(400)
        val nameEditTextLayout = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 0f, 1f).setDuration(400),
                ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.TRANSLATION_Y, 30f, 0f).setDuration(400)
            )
        }

        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 0f, 1f).setDuration(400)
        val emailEditTextLayout = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 0f, 1f).setDuration(400),
                ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.TRANSLATION_Y, 30f, 0f).setDuration(400)
            )
        }

        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 0f, 1f).setDuration(400)
        val passwordEditTextLayout = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 0f, 1f).setDuration(400),
                ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.TRANSLATION_Y, 30f, 0f).setDuration(400)
            )
        }

        val signupButton = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 0f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.signupButton, View.SCALE_X, 0.5f, 1f).setDuration(500),
                ObjectAnimator.ofFloat(binding.signupButton, View.SCALE_Y, 0.5f, 1f).setDuration(500)
            )
        }

        AnimatorSet().apply {
            playSequentially(
                titleTextView,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signupButton
            )
            start()
        }
    }



}
