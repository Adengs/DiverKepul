package com.codelabs.kepuldriver

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityEditProfileBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.ProfileResponse
import com.codelabs.kepuldriver.model.UpdateRequest
import com.codelabs.kepuldriver.model.UpdateResponse
import com.codelabs.kepuldriver.utils.InputStreamRequestBody
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    lateinit var sph: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
        editText()
    }

    override fun onResume() {
        super.onResume()
        getProfile()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
//            startActivity(Intent(this, Profile::class.java))
            updateProfile()
            binding.textName.clearFocus()
            binding.textEmail.clearFocus()
            binding.textPhone.clearFocus()
            binding.textAddress.clearFocus()
        }
        binding.editChangePass.setOnClickListener {
            startActivity(Intent(this, ChangePassword::class.java))
        }
    }

    private fun editText() {
        binding.textName.setOnClickListener {
            binding.textName.text?.length?.let { it1 -> binding.textName.setSelection(it1) }
        }
        binding.textEmail.setOnClickListener {
            binding.textEmail.text?.length?.let { it1 -> binding.textEmail.setSelection(it1) }
        }
        binding.textPhone.setOnClickListener {
            binding.textPhone.text?.length?.let { it1 -> binding.textPhone.setSelection(it1) }
        }
        binding.textAddress.setOnClickListener {
            binding.textAddress.text?.length?.let { it1 -> binding.textAddress.setSelection(it1) }
        }

    }

    private fun getProfile() {
        val image = binding.imageProfil
        sph = SharedPreference(this)
        binding.layCardProfileShimmer.visibility = View.VISIBLE
        binding.layCardProfile.visibility = View.GONE

        ApiMember.instanceRetrofit(this).getProfile().enqueue(object :
            Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    if (response.code() == 200) {
                        Glide.with(this@EditProfile)
                            .load(responseBody.data?.profile?.image.toString())
                            .into(image)
                        binding.textName.setText(responseBody.data?.profile?.name.toString())
                        binding.textEmail.setText(responseBody.data?.email.toString())
                        binding.textPhone.setText(responseBody.data?.profile?.phone.toString())
                        binding.textAddress.setText(Html.fromHtml(responseBody.data?.profile?.address?.replace("<p>","")))

                        binding.layCardProfileShimmer.visibility = View.GONE
                        binding.layCardProfile.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                t.printStackTrace()
                binding.layCardProfileShimmer.visibility = View.VISIBLE
                binding.layCardProfile.visibility = View.GONE
            }

        })
    }

    private fun validate(): Boolean {
        var allValid = true
        val phone = binding.textPhone.text.toString().trim()
        if (phone.isNotEmpty() && phone.length < 11){
            Toast.makeText(this, "No. Telepon minimal 11 karakter", Toast.LENGTH_LONG).show()
            allValid = false
        }

        if (phone.isNotEmpty() && phone.length > 14){
            Toast.makeText(this, "No. Telepon mamksimal 14 karakter", Toast.LENGTH_LONG).show()
            allValid = false
        }
        return allValid
    }

    private fun updateProfile() {
        if (!validate()) {
            return
        }

        val name = binding.textName.text.toString().trim()
        val email = binding.textEmail.text.toString().trim()
        val phone = binding.textPhone.text.toString().trim()
        val address = binding.textAddress.text.toString().trim()

        ApiMember.instanceRetrofit(this).updateProfile(
            UpdateRequest(
                name = name,
                email = email,
                phone = phone,
                address = address
            )
        ).enqueue(object : Callback<UpdateResponse>{
            override fun onResponse(
                call: Call<UpdateResponse>,
                response: Response<UpdateResponse>
            ) {
                val responseCode = response.code()
                val responseBody = response.body()

                if (responseCode == 200){
                    Toast.makeText(this@EditProfile, "Berhasil update profil", Toast.LENGTH_LONG).show()
                    Log.e("Auth", responseBody.toString())
                }
            }

            override fun onFailure(call: Call<UpdateResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@EditProfile, t.message, Toast.LENGTH_LONG).show()
            }

        })

    }

}