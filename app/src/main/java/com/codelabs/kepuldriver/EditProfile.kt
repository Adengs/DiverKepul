package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityEditProfileBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfile : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
    }

    override fun onResume() {
        super.onResume()
        getProfile()
    }
    private fun setEvent(){
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSave.setOnClickListener {
//            startActivity(Intent(this, Profile::class.java))
            onBackPressed()
        }
    }
    private fun getProfile(){
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

                if (responseBody != null){
                    if (responseBody.statusCode == "200"){
                        Glide.with(this@EditProfile)
                            .load(responseBody.data?.profile?.image.toString())
                            .into(image)
                        binding.textName.text = responseBody.data?.profile?.name.toString()
                        binding.textEmail.text = responseBody.data?.email.toString()
                        binding.textPhone.text = responseBody.data?.profile?.phone.toString()
                        binding.textAddress.text = responseBody.data?.profile?.address.toString()

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
}