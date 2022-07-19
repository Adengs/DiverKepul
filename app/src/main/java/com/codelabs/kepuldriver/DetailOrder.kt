package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.adapter.DetailOrderAdapter
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityDetailOrderBinding
import com.codelabs.kepuldriver.eventbus.OrderSelected
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.DetailResponse
import com.codelabs.kepuldriver.model.OrderResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailOrder : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderBinding
    private lateinit var detailOrderAdapter: DetailOrderAdapter
    lateinit var sph: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setAdapter()
        getDetail()

        setEvent()
    }

    private fun setAdapter() {
        detailOrderAdapter = DetailOrderAdapter()
        binding.recycleViewType.adapter = detailOrderAdapter
        binding.recycleViewType.setHasFixedSize(true)
        binding.recycleViewType.layoutManager = LinearLayoutManager(this)
    }

    private fun showOrder(data: List<DetailResponse.Data.Detail?>) {
        detailOrderAdapter.setData(data)
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getDetail() {
        sph = SharedPreference(this)
        val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val image = binding.imageProfilHome
        val ordercode = sph.fetchordercode().toString()
        val df = DecimalFormat("0.00")

        binding.layShimmer.visibility = View.VISIBLE
        binding.layOrder.visibility = View.GONE
        binding.shimmerType.visibility = View.VISIBLE
        binding.recycleViewType.visibility = View.GONE

        ApiMember.instanceRetrofit(this).getDetail(ordercode)
            .enqueue(object : Callback<DetailResponse> {

                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    val responseBody = response.body()
                    val totalkg = responseBody?.data?.estWeight?.div(1000)

                        if (responseBody != null) {
                        if (response.code() == 200) {
                            Log.e("Auth", responseBody.toString())
                            binding.statusText.text = responseBody.data?.status
                            binding.statusTime.text = responseBody.data?.shippingDate
                            binding.textOrderCode.text = responseBody.data?.reservationCode
                            Glide.with(this@DetailOrder)
                                .load(responseBody.data?.senderImage.toString())
                                .into(image)
                            binding.nameCust.text = responseBody.data?.senderName
//                            binding.textRange.text = df.format(
//                                distanceInKm(
//                                    lat1 = responseBody.data?.senderLat!!.toDouble(),
//                                    lat2 = responseBody.data?.recipientLat!!.toDouble(),
//                                    lon1 = responseBody.data?.senderLong!!.toDouble(),
//                                    lon2 = responseBody.data?.recipientLong!!.toDouble()
//                                )
//                            )
                            binding.textBudget.text =
                                rupiah.format(responseBody.data?.estTotal).toString()
                                    .replace(",00", "").replace("Rp", "")
                                    .replace(",", ".")
                            binding.addressSender.text = responseBody.data?.senderAddress
                            binding.addressReciper.text = responseBody.data?.recipientAddress
                            binding.range.text = df.format(
                                distanceInKm(
                                    lat1 = responseBody.data?.senderLat!!.toDouble(),
                                    lat2 = responseBody.data?.recipientLat!!.toDouble(),
                                    lon1 = responseBody.data?.senderLong!!.toDouble(),
                                    lon2 = responseBody.data?.recipientLong!!.toDouble()
                                )
                            )

                            responseBody.data?.detail?.let { showOrder(it) }
                            binding.totalBerat.text = totalkg.toString()
                            binding.btnTerima.setOnClickListener {
                                accOrder()
                            }


                            binding.layShimmer.visibility = View.GONE
                            binding.layOrder.visibility = View.VISIBLE
                            binding.shimmerType.visibility = View.GONE
                            binding.recycleViewType.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    t.printStackTrace()
                    binding.layShimmer.visibility = View.VISIBLE
                    binding.layOrder.visibility = View.GONE
                    binding.shimmerType.visibility = View.VISIBLE
                    binding.recycleViewType.visibility = View.GONE
                    Log.e("Auth3", t.message.toString())
                }

            })
    }

//    private fun getDetailOrder(){
//        binding.shimmerType.visibility = View.VISIBLE
//        binding.recycleViewType.visibility = View.GONE
//
//    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onEventSelected(data: OrderSelected) {
        sph.saveordercode(data.data?.reservationCode.toString())

    }

    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist =
            Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(
                deg2rad(lat2)
            ) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    private fun accOrder(){
        sph = SharedPreference(this)
        val ordercode = sph.fetchordercode().toString()

        ApiMember.instanceRetrofit(this).accOrder(ordercode).enqueue(object : Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null){
                    if (response.code() == 200){
                        Log.e("Auth", responseBody.toString())
                        Toast.makeText(this@DetailOrder, "Orderan berhasil diterima", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@DetailOrder, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}