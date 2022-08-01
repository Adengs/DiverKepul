package com.codelabs.kepuldriver.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
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
import com.codelabs.kepuldriver.model.ResponseUser
import com.google.android.gms.location.*
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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        context ?: return binding.root
        sph = SharedPreference(container?.context!!)

        binding.swipe.setOnRefreshListener {
            setAdapter()
            getOrder()
        }

        setAdapter()

        if (sph.getReady()){
            binding.switchHome.isChecked = true
        }else{
            binding.switchHome.isChecked = false
        }

        if (binding.switchHome.isChecked()){
            binding.textOnline.visibility = View.VISIBLE
            binding.textOffline.visibility = View.GONE
        }else{
            binding.textOnline.visibility = View.GONE
            binding.textOffline.visibility = View.VISIBLE
        }

        val context = requireContext()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        setSwitch(context)
//        getLocation(context)
        getLocationUpdates(context)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        setupPermission()
        setProfile()
        getOrder()
        startLocationUpdates()
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

                            if (activity != null){
                                Glide.with(this@HomeFragment)
                                    .load(responseBody.data?.profile?.image.toString())
                                    .into(image)
                            }

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

    private fun setSwitch(context : Context){
        sph = SharedPreference(context)
        binding.switchHome.setOnCheckedChangeListener { buttonView, isChecked ->
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
//                            getOrder()
                        }
                    }
                    override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                        t.printStackTrace()
                    }

                })

                binding.textOnline.visibility = View.VISIBLE
                binding.textOffline.visibility = View.GONE
                binding.recycleViewOrder.visibility = View.VISIBLE

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
                binding.recycleViewOrder.visibility = View.GONE

            }
        }
    }

//    @SuppressLint("MissingPermission")
//    private fun getLastKnownLocation(context: Context) {
//        sph = SharedPreference(context)
//        fusedLocationProviderClient.lastLocation
//            .addOnSuccessListener { location->
//                if (location != null) {
//                    // use your location object
//                    // get latitude , longitude and other info from this
//                    sph.savelatitude(location.latitude.toString())
//                    sph.savelongitude(location.longitude.toString())
//                }
//
//            }
//
//    }

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

    private fun setupPermission() {
        val permission = PermissionChecker.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionsResultCallback.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            Log.d("dawg", "already granted")
        }
    }

    private val permissionsResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { granted ->
        when (granted) {
            true -> {
                Log.d("dawg", "granted now via dialog")
            }
            false -> {
                Log.d("dawg", "denied via dialog")
            }
        }
    }

}