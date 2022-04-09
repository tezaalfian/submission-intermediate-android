package com.tezaalfian.storyapp.ui.signup

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
import com.tezaalfian.storyapp.data.UserRegister
import com.tezaalfian.storyapp.data.UserRepository
import com.tezaalfian.storyapp.data.local.UserPreference
import com.tezaalfian.storyapp.data.response.RegisterResponse
import com.tezaalfian.storyapp.data.retrofit.ApiConfig
import com.tezaalfian.storyapp.databinding.ActivitySignupBinding
import com.tezaalfian.storyapp.ui.UserViewModelFactory
import com.tezaalfian.storyapp.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.tezaalfian.storyapp.data.Result
import com.tezaalfian.storyapp.data.retrofit.ApiService
import kotlinx.coroutines.flow.collect

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        setupViewModel()
    }

    private fun setupViewModel() {
        signupViewModel = ViewModelProvider(
            this,
            UserViewModelFactory(UserRepository.getInstance(UserPreference.getInstance(dataStore)))
        )[SignupViewModel::class.java]
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
//                    signupViewModel.register(name, email, password).observe(this){ result ->
//                        if (result != null){
//                            when(result) {
//                                is Result.Loading -> {
//                                    binding.progressBar.visibility = View.VISIBLE
//                                }
//                                is Result.Success -> {
//                                    binding.progressBar.visibility = View.GONE
//                                    val user = result.data
//                                        if (user.error){
//                                            Toast.makeText(this@SignupActivity, user.message, Toast.LENGTH_SHORT).show()
//                                        }else{
//                                            AlertDialog.Builder(this@SignupActivity).apply {
//                                                setTitle("Yeah!")
//                                                setMessage("Your account successfully created!")
//                                                setPositiveButton("Next") { _, _ ->
//                                                    finish()
//                                                }
//                                                create()
//                                                show()
//                                            }
//                                        }
//                                }
//                                is Result.Error -> {
//                                    binding.progressBar.visibility = View.GONE
//                                    Toast.makeText(
//                                        this,
//                                        "Terjadi kesalahan" + result.error,
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            }
//                        }
//                    }
                    showLoading(true)
                    val client = ApiConfig.getApiService().register(name, email, password)
                    client.enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>,
                            response: Response<RegisterResponse>
                        ) {
                            showLoading(false)
                            if (response.isSuccessful) {
                                val result = response.body()
                                if (result != null) {
                                    if (result.error){
                                        Toast.makeText(this@SignupActivity, result.message, Toast.LENGTH_SHORT).show()
                                    }else{
                                        AlertDialog.Builder(this@SignupActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage(resources.getString(R.string.signup_success))
                                            setPositiveButton(resources.getString(R.string.next)) { _, _ ->
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