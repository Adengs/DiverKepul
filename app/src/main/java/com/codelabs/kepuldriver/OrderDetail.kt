package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.adapter.FotoVatidationAdapter
import com.codelabs.kepuldriver.adapter.OrderAdapter
import com.codelabs.kepuldriver.adapter.TypeValidationAdapter
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityOrderDetailBinding
import com.codelabs.kepuldriver.fragment.OrderFragment
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.DetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetail : AppCompatActivity() {
    private lateinit var binding : ActivityOrderDetailBinding
    private lateinit var typeValidationAdapter : TypeValidationAdapter
    private lateinit var fotoAdapter : FotoVatidationAdapter
    lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        var sample = ArrayList<String>()
        sample.add("1")
        sample.add("1")
        sample.add("1")
        sample.add("1")

        setAdapter1(sample)
        setAdapter2(sample)

        setEvent()
        getDetail()
    }

    private fun setAdapter1(data : List<String>) {
        typeValidationAdapter = TypeValidationAdapter(data)
        binding.recycleViewType.adapter = typeValidationAdapter
        binding.recycleViewType.setHasFixedSize(true)
        binding.recycleViewType.layoutManager = LinearLayoutManager(this)
    }

    private fun setAdapter2(data : List<String>) {
        fotoAdapter = FotoVatidationAdapter(data)
        binding.recycleViewFoto.adapter = fotoAdapter
        binding.recycleViewFoto.setHasFixedSize(true)
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
                            Glide.with(this@OrderDetail)
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
                            binding.addressSender.text = responseBody.data?.senderAddress
                            binding.range.text = df.format(
                                distanceInKm(
                                    lat1 = responseBody.data?.senderLat!!.toDouble(),
                                    lat2 = responseBody.data?.recipientLat!!.toDouble(),
                                    lon1 = responseBody.data?.senderLong!!.toDouble(),
                                    lon2 = responseBody.data?.recipientLong!!.toDouble()
                                )
                            )

//                            responseBody.data?.detail?.let { showOrder(it) }
                            binding.totalBerat.text = totalkg.toString()
//                            binding.btnTerima.setOnClickListener {
//                                accOrder()
//                            }


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
}