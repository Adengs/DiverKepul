package com.codelabs.kepuldriver

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.adapter.FotoVatidationAdapter
import com.codelabs.kepuldriver.adapter.TypeValidationAdapter
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ActivityOrderDetailBinding
import com.codelabs.kepuldriver.databinding.ItemTypeValidationBinding
import com.codelabs.kepuldriver.eventbus.OrderSelected
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.DetailResponse
import com.codelabs.kepuldriver.model.ProcessOrderRequest
import com.codelabs.kepuldriver.model.ResponseUser
import com.codelabs.kepuldriver.utils.InputStreamRequestBody
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.String
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDetail : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var typeValidationAdapter: TypeValidationAdapter
    private lateinit var fotoAdapter: FotoVatidationAdapter
    lateinit var sph: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setAdapter1()
        setAdapter2()

        setEvent()
        getDetail()

//        binding.lay1Foto.visibility = View.GONE
        binding.beratTotal.visibility = View.GONE
        binding.unitTotal.visibility = View.GONE
        binding.recycleViewFoto.visibility = View.GONE
    }

    private fun setAdapter1() {
        typeValidationAdapter = TypeValidationAdapter(object : OnChange{
            override fun onChangeListener() {
                setTotal()
            }

        })
        binding.recycleViewType.adapter = typeValidationAdapter
        binding.recycleViewType.setHasFixedSize(true)
        binding.recycleViewType.layoutManager = LinearLayoutManager(this)
    }

    private fun setAdapter2() {
        fotoAdapter = FotoVatidationAdapter()
        binding.recycleViewFoto.adapter = fotoAdapter
        binding.recycleViewFoto.setHasFixedSize(true)
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnUpload.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }

        }
        binding.btnProses.setOnClickListener {
            processOrder()
            onBackPressed()
            finish()
        }
    }

    private fun showOrder(data: List<DetailResponse.Data.Detail?>) {
        typeValidationAdapter.setData(data)
    }

    private fun showOrderFoto(data: List<DetailResponse.Data.Detail?>) {
        fotoAdapter.setFoto(data)
    }

    private fun getDetail() {
        sph = SharedPreference(this)
        val image = binding.imageProfilHome
        val ordercode = sph.fetchordercode().toString()
        val df = DecimalFormat("0.0")

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
                    val color = responseBody?.data?.reservationTypeColor

                    if (responseBody != null) {
                        if (response.code() == 200) {
                            Log.e("Auth", responseBody.toString())
                            binding.statusText.text = responseBody.data?.status
                            binding.statusTime.text = responseBody.data?.shippingDate
                            binding.textOrderCode.text = responseBody.data?.reservationCode
                            binding.textOrderStatus.text = responseBody.data?.reservationType
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor(color))
                            Glide.with(this@OrderDetail)
                                .load(responseBody.data?.senderImage.toString())
                                .into(image)
                            binding.nameCust.text = responseBody.data?.senderName
                            binding.textRange.text = df.format(
                                distanceInKm(
                                    lat1 = responseBody.data?.senderLat!!.toDouble(),
                                    lat2 = sph.fetchlatitude()!!.toDouble(),
                                    lon1 = responseBody.data?.senderLong!!.toDouble(),
                                    lon2 = sph.fetchlongitude()!!.toDouble()
                                )
                            )
                            binding.addressSender.text = responseBody.data?.senderAddress
                            binding.range.text = df.format(
                                distanceInKm(
                                    lat1 = responseBody.data?.senderLat!!.toDouble(),
                                    lat2 = responseBody.data?.recipientLat!!.toDouble(),
                                    lon1 = responseBody.data?.senderLong!!.toDouble(),
                                    lon2 = responseBody.data?.recipientLong!!.toDouble()
                                )
                            )

                            binding.rute.setOnClickListener {
                                val lat = responseBody.data?.senderLat!!
                                val long = responseBody.data?.senderLong!!

                                val gmmIntentUri = Uri.parse("google.navigation:q=${lat},${long}")
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                mapIntent.resolveActivity(packageManager)?.let {
                                    startActivity(mapIntent)
                                }

                                Log.e("maps", gmmIntentUri.toString())
                            }

                            responseBody.data?.detail?.let { showOrder(it) }
                            setTotal()
//                            responseBody.data?.detail?.let { showOrderFoto(it) }
                            binding.totalBerat.text = totalkg.toString()

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

    @SuppressLint("NotifyDataSetChanged")
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
        //                binding.fotoOrder.setImageURI(fileUri)
        //                uploadFoto(fileUri)
                    fotoAdapter.data.add(DetailResponse.Data.Detail(imagePath = fileUri))
                    fotoAdapter.notifyDataSetChanged()
                    binding.recycleViewFoto.visibility = View.VISIBLE
//                    showOrderFoto(listOf(DetailResponse.Data.Detail(imagePath = fileUri)))
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

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
        sph.saveordercodeaktif(data.data?.reservationCode.toString())

    }

    private fun setTotal() {
        sph = SharedPreference(this)
        val df = DecimalFormat("0.0")
        val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        val total = typeValidationAdapter.data
        var totalprice = 0
        for (i in 0 until total.size){
            totalprice += total[i]?.subtotal!!
        }
        var totalberat = 0
        for (i in 0 until total.size){
            totalberat += total[i]?.weight!!
        }
        val sum = totalberat.div(1000)

        var totalunit = 0
        for (i in 0 until total.size){
            totalunit += total[i]?.quantity!!
        }


        if (total[0]?.unit == "Unit") {
            binding.unitTotal.visibility = View.VISIBLE
            binding.unit.text = String.valueOf(totalunit)
        }
        if (total[0]?.unit == "Kilogram"){
            binding.beratTotal.visibility = View.VISIBLE
            binding.totalBerat.text = String.valueOf(sum).toString()
        }
        else {
            binding.beratTotal.visibility = View.VISIBLE
            binding.totalBerat.text = String.valueOf(sum).toString()
        }

        binding.totalHarga.text = rupiah.format(totalprice).toString().replace(",00", "").replace("Rp", "")
            .replace(",", ".")
    }

    interface OnChange{
        fun onChangeListener()
    }

    private fun processOrder(){
        sph = SharedPreference(this)
        val ordercode = sph.fetchordercodeaktif()
        val code = typeValidationAdapter.data[0]?.productCode!!
        var image : Uri? = null
        var imageBody: MultipartBody.Part? = null
        if (fotoAdapter.data.size > 0){
            image = fotoAdapter.data[0]?.imagePath!!
            val requestFile: RequestBody = InputStreamRequestBody(this, image)
            imageBody = MultipartBody.Part.createFormData("product[0][image]", image.toString(),requestFile)
        }

        val quantity = typeValidationAdapter.data[0]?.quantity.toString()
        val price = binding.totalHarga.text.toString()


        val data = mapOf(
            "product[0][code]" to code.toRequestBody(MultipartBody.FORM),
            "product[0][quantity]" to quantity.toRequestBody(MultipartBody.FORM),
            "price" to price.toRequestBody(MultipartBody.FORM)
        )

        ApiMember.instanceRetrofit(this).processOrder(ordercode, data, imageBody).enqueue(object : Callback<ResponseUser>{

            override fun onResponse(call: Call<ResponseUser>, response: Response<ResponseUser>) {
                val responseBody = response.body()
                if (responseBody != null){
                    if (response.code() == 200){
                        Log.e("cekdata" , response.body().toString())
                        Toast.makeText(this@OrderDetail, "Berhasil proses order", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}