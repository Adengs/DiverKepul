package com.codelabs.kepuldriver.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.codelabs.kepuldriver.DetailOrder
import com.codelabs.kepuldriver.OrderDetail
import com.codelabs.kepuldriver.R
import com.codelabs.kepuldriver.adapter.OrderAdapter
import com.codelabs.kepuldriver.adapter.OrderAktifAdapter
import com.codelabs.kepuldriver.adapter.OrderHistoryAdapter
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.FragmentOrderBinding
import com.codelabs.kepuldriver.eventbus.OrderSelected
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.OrderResponse
import com.codelabs.kepuldriver.model.ResponseUser
import com.google.android.gms.location.*
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var orderAdapter : OrderAdapter
    private lateinit var orderAktifAdapter: OrderAktifAdapter
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter
    lateinit var sph : SharedPreference
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        context ?: return binding.root
        sph = SharedPreference(container?.context!!)

        setAdapter1()
        setAdapter2()

        binding.shimmerOrder.visibility = View.VISIBLE
        binding.shimmerOrderAktif.visibility = View.GONE
        binding.shimmerOrderHistory.visibility = View.GONE
        binding.materialButtonAktif.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))
        binding.materialButtonHistory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_kepul)))

        val context = requireContext()
        onChecked(context)
        getOrder(context)
        getOrderAktif(context)

        if (sph.getReady()){
            binding.switchOrder.isChecked = true
        }else{
            binding.switchOrder.isChecked = false
        }

        if (binding.switchOrder.isChecked()){
            binding.textOnline.visibility = View.VISIBLE
            binding.textOffline.visibility = View.GONE
        }else{
            binding.textOnline.visibility = View.GONE
            binding.textOffline.visibility = View.VISIBLE

            binding.recycleViewOrderAktif.visibility = View.GONE
            binding.recycleViewOrder.visibility = View.GONE

            binding.materialButtonAktif.isEnabled = false
            binding.materialButtonHistory.isEnabled = false
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        setSwitch(context)
        getLocationUpdates(context)

        return binding.root
    }

    private fun onChecked(context : Context) {

        binding.toogleGroup.addOnButtonCheckedListener { toogle, checkedId, isChecked ->
//            var selected : Fragment = OrderAktifFragment()
            if (isChecked) {
                when (checkedId) {
                    R.id.material_button_aktif -> {
                        setAdapter1()
                        setAdapter2()
                        binding.shimmerOrder.visibility = View.VISIBLE
                        binding.recycleViewOrderAktif.visibility = View.GONE
                        binding.recycleViewOrder.visibility = View.GONE
                        binding.recycleViewOrderHistory.visibility = View.GONE
                        binding.shimmerOrderHistory.visibility = View.GONE
                        binding.shimmerOrderAktif.visibility = View.GONE
                        binding.materialButtonAktif.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))
                        binding.materialButtonHistory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_kepul)))
                        getOrder(context)
                        getOrderAktif(context)
                    }
                    R.id.material_button_history -> {
                        setAdapter3()
                        binding.shimmerOrderHistory.visibility = View.VISIBLE
                        binding.recycleViewOrder.visibility = View.GONE
                        binding.shimmerOrderAktif.visibility = View.GONE
                        binding.recycleViewOrderAktif.visibility = View.GONE
                        binding.recycleViewOrder.visibility = View.GONE
                        binding.recycleViewOrderHistory.visibility = View.GONE
                        binding.materialButtonAktif.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_kepul)))
                        binding.materialButtonHistory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))
                        getOrderHistory(context)
                    }
                }
            } else {
                if (toogle.checkedButtonId == View.NO_ID) {
                    Toast.makeText(view?.context, "Tidak ada yang dipilih", Toast.LENGTH_LONG)
                        .show()
                    binding.recycleViewOrderAktif.visibility = View.GONE
                    binding.recycleViewOrderHistory.visibility = View.GONE
                }
            }
        }
    }

    private fun setAdapter1() {
        orderAdapter = OrderAdapter()
        binding.recycleViewOrder.adapter = orderAdapter
        binding.recycleViewOrder.setHasFixedSize(true)
        binding.recycleViewOrder.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setAdapter2() {
        orderAktifAdapter = OrderAktifAdapter()
        binding.recycleViewOrderAktif.adapter = orderAktifAdapter
        binding.recycleViewOrderAktif.setHasFixedSize(true)
        binding.recycleViewOrderAktif.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setAdapter3() {
        orderHistoryAdapter = OrderHistoryAdapter()
        binding.recycleViewOrderHistory.adapter = orderHistoryAdapter
        binding.recycleViewOrderHistory.setHasFixedSize(true)
        binding.recycleViewOrderHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showOrder1(data: List<OrderResponse.Data?>) {
        orderAdapter.setData1(data)
    }

    private fun showOrder2(data: List<OrderResponse.Data?>) {
        orderAktifAdapter.setData2(data)
    }

    private fun showOrder3(data: List<OrderResponse.Data?>) {
        orderHistoryAdapter.setData3(data)
    }

    private fun getOrder(context : Context) {
        sph = SharedPreference(context)
        val param = mutableMapOf<String, String>()
        param["status"] = "Menunggu"

//        orderAktifAdapter.onClick = {
//            EventBus.getDefault().post(OrderSelected(it))
//            startActivity(Intent(context, OrderDetail::class.java))
//            it?.reservationCode?.let { it1 -> sph.saveordercode(it1) }
//        }
        orderAdapter.onClick = {
            EventBus.getDefault().post(OrderSelected(it))
            startActivity(Intent(context, DetailOrder::class.java))
            it?.reservationCode?.let { it1 -> sph.saveordercode(it1) }
        }

        ApiMember.instanceRetrofit(context).incomingOrder(param)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        if (response.code() == 200) {
                            responseBody.data?.let { showOrder1(it) }
//                            responseBody.data?.let { showOrder2(it) }
                            binding.shimmerOrder.visibility = View.GONE
                            binding.recycleViewOrderAktif.visibility = View.VISIBLE
                            binding.recycleViewOrder.visibility = View.VISIBLE
                            binding.recycleViewOrderHistory.visibility = View.GONE

                            Log.e("Auth", responseBody.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("Auth2", t.toString())
                    binding.shimmerOrder.visibility = View.VISIBLE
                    binding.recycleViewOrderAktif.visibility = View.GONE
                    binding.recycleViewOrder.visibility = View.GONE
                    binding.recycleViewOrderHistory.visibility = View.GONE

                }

            })
    }

    private fun getOrderAktif(context : Context) {
        sph = SharedPreference(context)
        val param = mutableMapOf<String, String>()
        param["status"] = "Dijemput"

        orderAktifAdapter.onClick = {
            EventBus.getDefault().post(OrderSelected(it))
            startActivity(Intent(context, OrderDetail::class.java))
            it?.reservationCode?.let { it1 -> sph.saveordercodeaktif(it1) }
        }
//        orderAdapter.onClick = {
//            EventBus.getDefault().post(OrderSelected(it))
//            startActivity(Intent(context, DetailOrder::class.java))
//            it?.reservationCode?.let { it1 -> sph.saveordercode(it1) }
//        }

        ApiMember.instanceRetrofit(context).incomingOrder(param)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        if (response.code() == 200) {
//                            responseBody.data?.let { showOrder1(it) }
                            responseBody.data?.let { showOrder2(it) }
                            binding.shimmerOrder.visibility = View.GONE
                            binding.recycleViewOrderAktif.visibility = View.VISIBLE
                            binding.recycleViewOrder.visibility = View.VISIBLE
                            binding.recycleViewOrderHistory.visibility = View.GONE

                            Log.e("Auth", responseBody.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("Auth2", t.toString())
                    binding.shimmerOrder.visibility = View.VISIBLE
                    binding.recycleViewOrderAktif.visibility = View.GONE
                    binding.recycleViewOrder.visibility = View.GONE
                    binding.recycleViewOrderHistory.visibility = View.GONE

                }

            })
    }

    private fun getOrderHistory(context : Context) {
        sph = SharedPreference(context)
        val param = mutableMapOf<String, String>()
        param["status"] = "Selesai"

        ApiMember.instanceRetrofit(context).incomingOrder(param)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(
                    call: Call<OrderResponse>,
                    response: Response<OrderResponse>
                ) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        if (response.code() == 200) {
                            responseBody.data?.let { showOrder3(it) }
                            binding.shimmerOrderHistory.visibility = View.GONE
                            binding.shimmerOrder.visibility = View.GONE
                            binding.shimmerOrderAktif.visibility = View.GONE
                            binding.recycleViewOrderAktif.visibility = View.GONE
                            binding.recycleViewOrderHistory.visibility = View.VISIBLE
                            binding.recycleViewOrder.visibility = View.GONE

                            Log.e("Auth", responseBody.toString())
                        }
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("Auth2", t.toString())
                    binding.shimmerOrderHistory.visibility = View.VISIBLE
                    binding.shimmerOrder.visibility = View.GONE
                    binding.shimmerOrderAktif.visibility = View.GONE
                    binding.recycleViewOrderHistory.visibility = View.GONE
                    binding.recycleViewOrder.visibility = View.GONE
                    binding.recycleViewOrderAktif.visibility = View.GONE

                }

            })
    }

    private fun setSwitch(context : Context){
        sph = SharedPreference(context)
        binding.switchOrder.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                ApiMember.instanceRetrofit(view?.context!!).hitSwitch().enqueue(object : Callback<ResponseUser>{
                    override fun onResponse(
                        call: Call<ResponseUser>,
                        response: Response<ResponseUser>
                    ) {
                        val responseBody = response.body()
                        if (response.code() == 200){
                            Log.e("SwitchReady", responseBody.toString())
                            sph.ready(true)
//                            getLocation(context)
                            getLocationUpdates(context)
                        }
                    }
                    override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                        t.printStackTrace()
                    }

                })

                binding.textOnline.visibility = View.VISIBLE
                binding.textOffline.visibility = View.GONE
                binding.recycleViewOrderAktif.visibility = View.VISIBLE
                binding.recycleViewOrder.visibility = View.VISIBLE
                binding.recycleViewOrderHistory.visibility = View.VISIBLE

                binding.materialButtonAktif.isEnabled = true
                binding.materialButtonHistory.isEnabled = true

            } else {
                ApiMember.instanceRetrofit(view?.context!!).hitSwitch().enqueue(object : Callback<ResponseUser>{
                    override fun onResponse(
                        call: Call<ResponseUser>,
                        response: Response<ResponseUser>
                    ) {
                        val responseBody = response.body()
                        if (response.code() == 200){
                            Log.e("SwitchNotReady", responseBody.toString())
                            sph.ready(false)
                        }
                    }
                    override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                        t.printStackTrace()
                    }

                })

                binding.textOnline.visibility = View.GONE
                binding.textOffline.visibility = View.VISIBLE
                binding.recycleViewOrderAktif.visibility = View.GONE
                binding.recycleViewOrder.visibility = View.GONE

                binding.materialButtonAktif.isEnabled = false
                binding.materialButtonHistory.isEnabled = false
            }
        }
    }

//    private fun getLocation(context: Context) {
//        sph = SharedPreference(context)
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
//            != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
//            return
//        }
//
//        val location = fusedLocationProviderClient.lastLocation
//        location.addOnSuccessListener {
//            if (it != null){
//                val textLatitude = it.latitude.toString()
//                val textLongitude = it.longitude.toString()
//                sph.savelatitude(textLatitude)
//                sph.savelongitude(textLongitude)
//            }
//        }
//
//    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    @SuppressLint("RestrictedApi")
    private fun getLocationUpdates(context: Context) {
        sph = SharedPreference(context)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest()
        locationRequest.interval = 50000
        locationRequest.fastestInterval = 50000
        locationRequest.smallestDisplacement = 170f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return

                if (locationResult.locations.isNotEmpty()) {
                    // get latest location
                    val location =
                        locationResult.lastLocation
                    // use your location object
                    // get latitude , longitude and other info from this
                    sph.savelatitude(location.latitude.toString())
                    sph.savelongitude(location.longitude.toString())
                }


            }
        }
    }

    //start location updates
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    // stop location updates
    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    // stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}