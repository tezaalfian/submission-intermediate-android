package com.tezaalfian.storyapp.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.tezaalfian.storyapp.R
import com.tezaalfian.storyapp.data.UserRepository
import com.tezaalfian.storyapp.data.response.LoginResponse
import com.tezaalfian.storyapp.data.retrofit.ApiConfig
import com.tezaalfian.storyapp.databinding.ActivityLoginBinding
import com.tezaalfian.storyapp.ui.UserViewModelFactory
import com.tezaalfian.storyapp.ui.main.MainActivity
import com.tezaalfian.storyapp.ui.signup.SignupActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

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
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edtEmail.error = resources.getString(R.string.message_validation, "email")
                }
                password.isEmpty() -> {
                    binding.edtPassword.error = resources.getString(R.string.message_validation, "password")
                }
                else -> {
                    showLoading(true)
                    val client = ApiConfig.getApiService().login(email, password)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            showLoading(false)
                            if (response.isSuccessful) {
                                val result = response.body()
                                if (result != null) {
                                    if (result.error){
                                        Toast.makeText(this@LoginActivity, result.message, Toast.LENGTH_SHORT).show()
                                    }else{
                                        val token = result.loginResult?.token ?: ""
                                        loginViewModel.setToken(token)
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
                            } else {
                                Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            showLoading(false)
                            Log.d("Register", t.message.toString())
                            Toast.makeText(this@LoginActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository.getInstance(dataStore))
        )[LoginViewModel::class.java]

        loginViewModel.getToken().observe(this){ token ->
            if (token.isNotEmpty()){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}