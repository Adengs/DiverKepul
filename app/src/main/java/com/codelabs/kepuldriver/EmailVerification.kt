package com.codelabs.kepuldriver

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityEmailVerificationBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.EmailRequest
import com.codelabs.kepuldriver.model.EmailVerificationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailVerification : AppCompatActivity() {
    private lateinit var binding : ActivityEmailVerificationBinding
    lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
        setTextEmail()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, ForgetPassword::class.java))
        }
        binding.btnSend.setOnClickListener {
            verifCode()
        }
        binding.resendCode.setOnClickListener {
            resendCode()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTextEmail(){
        sph = SharedPreference(this)
        val email = sph.fetchemail()
        val p = """^([^@]{4})([^@]+)""".toRegex()
        val result = email?.replace(p) {
            it.groupValues[1] + "*".repeat(it.groupValues[2].length)
        }
        binding.text.text = "Masukan kode verifikasi yang kami kirimkan ke email ${result}"
    }

    private fun verifCode() {
        sph = SharedPreference(this)
        val code = binding.textCode.text.toString().trim()
        val verifcode = sph.fetchcodee().toString().trim()

        if (code.isEmpty()){
            binding.layCode.error = "Code tidak boleh kosong"
            binding.textCode.requestFocus()
            return
        } else if (code.isNotEmpty() && code != verifcode){
            Toast.makeText(this, "Kode yang dimasukan tidak sesuai", Toast.LENGTH_LONG).show()
            return
        }

        startActivity(Intent(this, SetPassword::class.java))
    }

    private fun resendCode() {
        sph = SharedPreference(this)
        val email = sph.fetchemail().toString().trim()

        ApiMember.instanceRetrofit(this).email(EmailRequest(email = email)).enqueue(object :
            Callback<EmailVerificationResponse> {
            override fun onResponse(
                call: Call<EmailVerificationResponse>,
                response: Response<EmailVerificationResponse>
            ) {
                val responseBody = response.body()

                if (response.code() == 200){
                    Log.e("Auth", responseBody.toString())
                    responseBody?.data?.driver?.code?.let { sph.savecode(it) }
                    startActivity(Intent(this@EmailVerification, EmailVerification::class.java))
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