package com.tezaalfian.storyapp.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.tezaalfian.storyapp.R
import com.tezaalfian.storyapp.data.UserRegister
import com.tezaalfian.storyapp.data.response.RegisterResponse
import com.tezaalfian.storyapp.data.retrofit.ApiConfig
import com.tezaalfian.storyapp.databinding.ActivitySignupBinding
import com.tezaalfian.storyapp.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
    }

    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.edtName.error = resources.getString(R.string.message_validation, "name")
                }
                email.isEmpty() -> {
                    binding.edtEmail.error = resources.getString(R.string.message_validation, "email")
                }
                password.isEmpty() -> {
                    binding.edtPassword.error = resources.getString(R.string.message_validation, "password")
                }
                else -> {
                    showLoading(true)
                    val client = ApiConfig.getApiService().register(name, email, password)
                    client.enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            showLoading(false)
                            Log.d("Register", response.toString())
                            if (response.isSuccessful) {
                                val result = response.body()
                                if (result != null) {
                                    if (result.error){
                                        Toast.makeText(this@SignupActivity, result.message, Toast.LENGTH_SHORT).show()
                                    }else{
                                        AlertDialog.Builder(this@SignupActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage("Your account successfully created!")
                                            setPositiveButton("Next") { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(this@SignupActivity, response.message(), Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            showLoading(false)
                            Log.d("Register", t.message.toString())
                            Toast.makeText(this@SignupActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        binding.tvLogin.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}