package com.example.sub1int2.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sub1int2.data.pref.UserModel
import com.example.sub1int2.databinding.ActivityLoginBinding
import com.example.sub1int2.view.ViewModelFactory
import com.example.sub1int2.view.main.MainActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.sub1int2.data.api.ApiConfig

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setupView()
        setupAction()
        setupValidation()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            if (validateInput()) {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString()

                // Disable button to prevent multiple requests
                binding.loginButton.isEnabled = false

                lifecycleScope.launch {
                    try {
                        val response = ApiConfig.getApiService { "" }.login(email, password)

                        binding.loginButton.isEnabled = true

                        // Check if login was successful using response
                        if (response.error == false) {
                            // Save user session with actual token from response
                            viewModel.saveSession(UserModel(email, response.loginResult?.token ?: ""))

                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Yeah!")
                                setMessage("${response.message ?: "Login berhasil"}. Anda berhasil login. Sudah tidak sabar untuk belajar ya?")
                                setPositiveButton("Lanjut") { _, _ ->
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        } else {
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle("Error")
                                setMessage(response.message ?: "Login gagal. Silakan coba lagi.")
                                setPositiveButton("OK", null)
                                create()
                                show()
                            }
                        }
                    } catch (e: Exception) {
                        binding.loginButton.isEnabled = true

                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle("Error")
                            setMessage("${e.message}\nSilakan coba lagi.")
                            setPositiveButton("OK", null)
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun setupValidation() {
        val emailTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }
        }

        val passwordTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }
        }

        binding.emailEditText.addTextChangedListener(emailTextWatcher)
        binding.passwordEditText.addTextChangedListener(passwordTextWatcher)
    }

    private fun validateEmail(email: String): Boolean {
        val trimmedEmail = email.trim()
        return if (trimmedEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches()) {
            binding.emailEditTextLayout.error = "Masukkan alamat email yang benar"
            false
        } else {
            binding.emailEditTextLayout.error = null
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.length < 8) {
            binding.passwordEditTextLayout.error = "Password tidak boleh kurang dari 8 karakter"
            false
        } else {
            binding.passwordEditTextLayout.error = null
            true
        }
    }

    private fun validateInput(): Boolean {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        return isEmailValid && isPasswordValid
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }

}