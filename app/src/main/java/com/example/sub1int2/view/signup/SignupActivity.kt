package com.example.sub1int2.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.text.TextWatcher
import android.text.Editable
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sub1int2.databinding.ActivitySignupBinding
import androidx.lifecycle.lifecycleScope
import com.example.sub1int2.data.api.ApiConfig
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setupView()
        setupValidation()
        setupAction()
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

    private fun setupValidation() {
        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                validateName(s.toString())
            }
        }

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

        binding.nameEditText.addTextChangedListener(nameTextWatcher)
        binding.emailEditText.addTextChangedListener(emailTextWatcher)
        binding.passwordEditText.addTextChangedListener(passwordTextWatcher)
    }

    private fun validateName(name: String): Boolean {
        return if (name.trim().isEmpty()) {
            binding.nameEditTextLayout.error = "Nama tidak boleh kosong"
            false
        } else {
            binding.nameEditTextLayout.error = null
            true
        }
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
        val name = binding.nameEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        val isNameValid = validateName(name)
        val isEmailValid = validateEmail(email)
        val isPasswordValid = validatePassword(password)

        return isNameValid && isEmailValid && isPasswordValid
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            if (validateInput()) {
                val name = binding.nameEditText.text.toString().trim()
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString()

                // Disable button to prevent multiple requests
                binding.signupButton.isEnabled = false

                lifecycleScope.launch {
                    try {
                        val response = ApiConfig.getApiService { "" }.register(name, email, password)

                        binding.signupButton.isEnabled = true

                        // Check if registration was successful using response
                        if (response.error == false) {
                            AlertDialog.Builder(this@SignupActivity).apply {
                                setTitle("Yeah!")
                                setMessage(response.message ?: "Akun dengan $email sudah jadi nih. Yuk, login dan belajar coding.")
                                setPositiveButton("Lanjut") { _, _ ->
                                    finish()
                                }
                                create()
                                show()
                            }
                        } else {
                            AlertDialog.Builder(this@SignupActivity).apply {
                                setTitle("Error")
                                setMessage(response.message ?: "Gagal membuat akun. Silakan coba lagi.")
                                setPositiveButton("OK", null)
                                create()
                                show()
                            }
                        }
                    } catch (e: Exception) {
                        binding.signupButton.isEnabled = true

                        AlertDialog.Builder(this@SignupActivity).apply {
                            setTitle("Error")
                            setMessage("Gagal membuat akun. Silakan coba lagi.")
                            setPositiveButton("OK", null)
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}