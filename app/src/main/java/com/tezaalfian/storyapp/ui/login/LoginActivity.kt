package com.tezaalfian.storyapp.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.tezaalfian.storyapp.R
import com.tezaalfian.storyapp.databinding.ActivityLoginBinding
import com.tezaalfian.storyapp.ui.UserViewModelFactory
import com.tezaalfian.storyapp.ui.main.MainActivity
import com.tezaalfian.storyapp.ui.signup.SignupActivity
import com.tezaalfian.storyapp.data.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        setupViewModel()
    }

    private fun setupAction() {
        binding.tvNewSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            when {
                email.isEmpty() -> {
                    binding.edtEmail.error = resources.getString(R.string.message_validation, "email")
                }
                password.isEmpty() -> {
                    binding.edtPassword.error = resources.getString(R.string.message_validation, "password")
                }
                else -> {
                    loginViewModel.login(email, password).observe(this){result ->
                        if (result != null){
                            when(result) {
                                is Result.Loading -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    binding.progressBar.visibility = View.GONE
                                    val user = result.data
                                    if (user.error){
                                        Toast.makeText(this@LoginActivity, user.message, Toast.LENGTH_SHORT).show()
                                    }else{
                                        val token = user.loginResult?.token ?: ""
                                        loginViewModel.setToken(token, true)
                                        AlertDialog.Builder(this@LoginActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage(resources.getString(R.string.login_success))
                                            setPositiveButton(resources.getString(R.string.next)) { _, _ ->
                                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                startActivity(intent)
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                                is Result.Error -> {
                                    binding.progressBar.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        "Terjadi kesalahan" + result.error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupViewModel() {
        val factory: UserViewModelFactory = UserViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this){ token ->
            if (token.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}