package com.codelabs.kepuldriver.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.DetailOrder
import com.codelabs.kepuldriver.adapter.OrderAdapter
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.FragmentHomeBinding
import com.codelabs.kepuldriver.eventbus.OrderSelected
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.OrderResponse
import com.codelabs.kepuldriver.model.ProfileResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    var value = ""
    private lateinit var binding: FragmentHomeBinding
    private lateinit var orderAdapter: OrderAdapter
    lateinit var sph: SharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root

        binding.swipe.setOnRefreshListener {
            setAdapter()
            getOrder()
        }

        setAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        setProfile()
        getOrder()
    }

    private fun setAdapter() {
        orderAdapter = OrderAdapter()
        binding.recycleViewOrder.setHasFixedSize(true)
        binding.recycleViewOrder.adapter = orderAdapter
        binding.recycleViewOrder.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setProfile() {
        val image = binding.imageProfilHome
        binding.shimmerProfileHome.visibility = View.VISIBLE
        binding.layProfilHome.visibility = View.GONE

        ApiMember.instanceRetrofit(view?.context!!).getProfile()
            .enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        if (response.code() == 200) {
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

    private fun showOrder(data: List<OrderResponse.Data?>) {
        orderAdapter.setData1(data)
    }

    private fun getOrder() {
        val param = mutableMapOf<String, String>()
        param["status"] = "Menunggu"
        sph = SharedPreference(view?.context!!)
        binding.shimmerOrder.visibility = View.VISIBLE
        binding.recycleViewOrder.visibility = View.GONE

        orderAdapter.onClick = {
            EventBus.getDefault().post(OrderSelected(it))
            startActivity(Intent(view?.context, DetailOrder::class.java))
            it?.reservationCode?.let { it1 -> sph.saveordercode(it1) }
        }

        ApiMember.instanceRetrofit(view?.context!!).incomingOrder(param)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        if (response.code() == 200) {
                            responseBody.data?.let { showOrder(it) }
                            binding.swipe.isRefreshing = false
                            binding.shimmerOrder.visibility = View.GONE
                            binding.recycleViewOrder.visibility = View.VISIBLE
                            Log.e("Auth", responseBody.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("Auth2", t.toString())
                    binding.swipe.isRefreshing = false
                    binding.shimmerOrder.visibility = View.VISIBLE
                    binding.recycleViewOrder.visibility = View.GONE
                }

            })
    }

    private fun acceptOrder(){

    }

}