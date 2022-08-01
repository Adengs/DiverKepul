package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityForgetPasswordBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.EmailRequest
import com.codelabs.kepuldriver.model.EmailVerificationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        binding.btnSend.setOnClickListener {
            binding.textEmail.clearFocus()
            getCode()
        }
    }


    private fun getCode() {
        sph = SharedPreference(this)
        val email = binding.textEmail.text.toString().trim()

        if (email.isEmpty()){
            binding.layEmail.error = "Email tidak boleh kosong"
            binding.textEmail.requestFocus()
            return
        }

        ApiMember.instanceRetrofit(this).email(EmailRequest(email = email)).enqueue(object :
            Callback<EmailVerificationResponse> {
            override fun onResponse(
                call: Call<EmailVerificationResponse>,
                response: Response<EmailVerificationResponse>
            ) {
                val responseBody = response.body()

                if (response.code() == 200){
                    Log.e("Auth", responseBody.toString())
                    sph.saveemail(email)
                    responseBody?.data?.driver?.code?.let { sph.savecode(it) }
                    startActivity(Intent(this@ForgetPassword, EmailVerification::class.java))
                }else {
                    Log.e("Auth", responseBody.toString())
                }
            }

            override fun onFailure(call: Call<EmailVerificationResponse>, t: Throwable) {
                t.printStackTrace()
                Log.e("Auth", t.message.toString())
            }

        })
    }
}