package com.codelabs.kepuldriver.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.*
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.FragmentProfileBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {
    lateinit var sph : SharedPreference
    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        context ?: binding.root

        setEvent()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setProfile()
    }
    private fun setEvent(){
        binding.layEditProfil.setOnClickListener {
            startActivity(Intent(view?.context, EditProfile::class.java))
        }
        binding.layEditNotif.setOnClickListener {
            startActivity(Intent(view?.context, Notification::class.java))
        }
        binding.layLogout.setOnClickListener {
            startActivity(Intent(view?.context, Login::class.java))
            sph.getBoolean()
        }
    }

    private fun setProfile() {
        val image = binding.imageProfilHome
        val icnvehicle = binding.icnVehicle
        sph = SharedPreference(view?.context!!)
        binding.layProfilProfilShimmer.visibility = View.VISIBLE
        binding.layProfilProfil.visibility = View.GONE
        binding.vehicleShimmer.visibility = View.VISIBLE
        binding.vehicle.visibility = View.GONE

        ApiMember.instanceRetrofit(view?.context!!).getProfile().enqueue(object :
            Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null){
                    if (response.code() == 200){
                        Glide.with(this@ProfileFragment)
                            .load(responseBody.data?.profile?.image.toString())
                            .into(image)
                        binding.textName.text = responseBody.data?.profile?.name.toString()
                        Glide.with(this@ProfileFragment)
                            .load(responseBody.data?.transportation?.image.toString())
                            .into(icnvehicle)
                        binding.nameVehicle.text = responseBody.data?.transportation?.name.toString()

                        binding.layProfilProfilShimmer.visibility = View.GONE
                        binding.layProfilProfil.visibility = View.VISIBLE
                        binding.vehicleShimmer.visibility = View.GONE
                        binding.vehicle.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                t.printStackTrace()
                binding.layProfilProfilShimmer.visibility = View.VISIBLE
                binding.layProfilProfil.visibility = View.GONE
                binding.vehicleShimmer.visibility = View.VISIBLE
                binding.vehicle.visibility = View.GONE
            }

        })
    }
}