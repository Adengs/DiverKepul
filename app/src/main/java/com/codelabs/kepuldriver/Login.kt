package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityLoginBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.RequestUser
import com.codelabs.kepuldriver.model.ResponseUser
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    lateinit var sph: SharedPreference
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
    }

    private fun setEvent() {
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.textForget.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }
    }

    private fun login() {
        sph = SharedPreference(this)
        val email = binding.textEmail.text.toString().trim()
        val password = binding.textPass.text.toString().trim()

        if (email.isEmpty()) {
            binding.layEmail.error = "Email tidak boleh kosong"
            binding.textEmail.requestFocus()
            return
        } else if (password.isEmpty()) {
            binding.layPass.error = "Password tidak boleh kosong"
            binding.textPass.requestFocus()
            return
        }

        ApiMember.instanceRetrofit(this).login(RequestUser(email = email, password = password))
            .enqueue(object : Callback<ResponseUser> {
                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    val logresponse = response.body()

                    if (logresponse != null) {
                        if (logresponse.statusCode == "200") {
                            Toast.makeText(this@Login, logresponse.message, Toast.LENGTH_LONG)
                                .show()
                            sph.saveAuthToken(logresponse.data?.token.toString())
                            sph.savePassword(binding.textPass.text.toString())
                            startActivity(Intent(this@Login, MainActivity::class.java))
                            finish()
                            Log.e("Auth", logresponse.toString())
                            sph.put(login = true)
                        } else {
                            response.errorBody()?.let { error ->
                                val parsing =
                                    Gson().fromJson(error.string(), ResponseUser::class.java)
                                Toast.makeText(this@Login, parsing.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    } else {
                        response.errorBody()?.let { error ->
                            val parsing = Gson().fromJson(error.string(), ResponseUser::class.java)
                            Toast.makeText(this@Login, parsing.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@Login, t.message, Toast.LENGTH_LONG).show()
                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}