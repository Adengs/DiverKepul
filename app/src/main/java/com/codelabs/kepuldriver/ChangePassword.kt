package com.codelabs.kepuldriver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityChangePasswordBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.PasswordRequest
import com.codelabs.kepuldriver.model.UpdateRequest
import com.codelabs.kepuldriver.model.UpdateResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword : AppCompatActivity() {
    private lateinit var binding : ActivityChangePasswordBinding
    lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSubmit.setOnClickListener {
            binding.textOldpass.clearFocus()
            binding.textNewpass.clearFocus()
            binding.textConpass.clearFocus()
            updatePassword()
        }
    }

    private fun validate() : Boolean{
        var allValid = true
        sph = SharedPreference(this)
        val conpassold = sph.fetchPassword().toString().trim()
        val oldpass = binding.textOldpass.text.toString().trim()
        val newpass = binding.textNewpass.text.toString().trim()
        val conpass = binding.textConpass.text.toString().trim()

        //validasi kolom kosong
        if (oldpass.isEmpty()){
            binding.layOldpass.error = "Kolom tidak boleh kosong"
            binding.textOldpass.requestFocus()
            allValid = false
        }
        if (newpass.isEmpty()){
            binding.layNewpass.error = "Kolom tidak boleh kosong"
            binding.textNewpass.requestFocus()
            allValid = false
        }
        if (conpass.isEmpty()){
            binding.layConpass.error = "Kolom tidak boleh kosong"
            binding.textConpass.requestFocus()
            allValid = false
        }

        //validasi password
        if (oldpass.isNotEmpty() && oldpass != conpassold){
            binding.layOldpass.error = "Password lama salah"
            binding.textOldpass.requestFocus()
            allValid = false
        }
        if (newpass.isNotEmpty() && conpass.isNotEmpty() && newpass != conpass){
            Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_LONG).show()
            allValid = false
        }
        if (oldpass.isNotEmpty() && newpass.isNotEmpty() && oldpass == newpass){
            Toast.makeText(this, "Password baru tidak boleh sama dengan password lama", Toast.LENGTH_LONG).show()
            allValid = false
        }

        //validasi password karakter
        if (newpass.isNotEmpty()  && newpass.length < 6 ){
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_LONG).show()
            allValid = false
        }
        if (conpass.isNotEmpty() && conpass.length < 6 ){
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_LONG).show()
            allValid = false
        }
        if (oldpass.isNotEmpty() && oldpass.length < 6){
            Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_LONG).show()
            allValid = false
        }

        return allValid
    }

    private fun updatePassword() {
        if (!validate()){
            return
        }

        val newpass = binding.textNewpass.text.toString().trim()

        ApiMember.instanceRetrofit(this).updatePassword(
            PasswordRequest(
                password = newpass
            )
        ).enqueue(object : Callback<UpdateResponse> {
            override fun onResponse(
                call: Call<UpdateResponse>,
                response: Response<UpdateResponse>
            ) {
                val responseCode = response.code()
                val responseBody = response.body()

                if (responseCode == 200){
                    Toast.makeText(this@ChangePassword, "Berhasil ubah password", Toast.LENGTH_LONG).show()
                    sph.savePassword(newpass)
                    onBackPressed()
                    Log.e("Auth", responseBody.toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ChangePassword, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}