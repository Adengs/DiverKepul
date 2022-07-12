package com.codelabs.kepuldriver.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.DetailOrder
import com.codelabs.kepuldriver.adapter.OrderAdapter
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.FragmentHomeBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: OrderAdapter
    lateinit var sph : SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root
        var sample = ArrayList<String>()
        sample.add("1")
        sample.add("1")
        sample.add("1")

        setAdapter(sample)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        setProfile()
    }

    private fun setAdapter(data: List<String>) {
        adapter = OrderAdapter(data)
        binding.recycleViewOrder.adapter = adapter
        binding.recycleViewOrder.setHasFixedSize(true)
        adapter.onClick = {
            startActivity(Intent(view?.context, DetailOrder::class.java))
        }
    }

    private fun setProfile() {
        val image = binding.imageProfilHome
        sph = SharedPreference(view?.context!!)
        binding.shimmerProfileHome.visibility = View.VISIBLE
        binding.layProfilHome.visibility = View.GONE

        ApiMember.instanceRetrofit(view?.context!!).getProfile().enqueue(object : Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null){
                    if (responseBody.statusCode == "200"){
                        Glide.with(this@HomeFragment)
                            .load(responseBody.data?.profile?.image.toString())
                            .into(image)
                        binding.textName.text = responseBody.data?.profile?.name.toString()

                        binding.shimmerProfileHome.visibility = View.GONE
                        binding.layProfilHome.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                t.printStackTrace()
                binding.shimmerProfileHome.visibility = View.VISIBLE
                binding.layProfilHome.visibility = View.GONE
            }

        })
    }
}