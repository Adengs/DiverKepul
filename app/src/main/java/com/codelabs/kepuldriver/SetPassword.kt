package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityEmailVerificationBinding
import com.codelabs.kepuldriver.databinding.ActivitySetPasswordBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.RequestUser
import com.codelabs.kepuldriver.model.ResponseUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SetPassword : AppCompatActivity() {
    private lateinit var binding : ActivitySetPasswordBinding
    lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, EmailVerification::class.java))
        }
        binding.btnSubmit.setOnClickListener {
            setPassword()
        }
    }

    private fun validate(): Boolean {
        var allValid = true
        val newpass = binding.textNewpass.text.toString().trim()
        val conpass = binding.textConpass.text.toString().trim()

        if (newpass.isEmpty()) {
            binding.layNewpass.error = "Kolom tidak boleh kosong"
            binding.textNewpass.requestFocus()
            allValid = false
        }
        if (conpass.isEmpty()) {
            binding.layConpass.error = "Kolom tidak boleh kosong"
            binding.textConpass.requestFocus()
            allValid = false
        }

        //validasi password
        if (newpass.isNotEmpty() && conpass.isNotEmpty() && newpass != conpass) {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_LONG).show()
            allValid = false
        }
        if (newpass.isNotEmpty() && newpass.length < 6) {
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_LONG).show()
            allValid = false
        }
//        if (conpass.isNotEmpty() && conpass.length < 6){
//            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_LONG).show()
//            allValid = false
//        }

        return allValid
    }

    private fun setPassword() {
        if (!validate()) {
            return
        }
        sph = SharedPreference(this)
        val email = sph.fetchemail().toString().trim()
        val password = binding.textConpass.text.toString().trim()

        ApiMember.instanceRetrofit(this)
            .changePassword(RequestUser(email = email, password = password))
            .enqueue(object : Callback<ResponseUser> {

                override fun onResponse(
                    call: Call<ResponseUser>,
                    response: Response<ResponseUser>
                ) {
                    val responseBody = response.body()
                    if (response.code() == 200){
                        Log.e("Auth", responseBody.toString())
                        Toast.makeText(this@SetPassword, "Berhasil ubah password, silahkan login", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@SetPassword, Login::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@SetPassword, t.message, Toast.LENGTH_LONG).show()
                }

            })
    }
}