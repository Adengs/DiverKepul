package com.codelabs.kepuldriver.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.api.ApiMember
import com.codelabs.kepuldriver.databinding.ItemOrderBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.DetailResponse
import com.codelabs.kepuldriver.model.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderAdapter(val data: ArrayList<OrderResponse.Data?> = arrayListOf()) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>(){
    //, Filterable
    var datafilter : ArrayList<OrderResponse.Data?> = arrayListOf()
    var onClick: ((OrderResponse.Data?) -> Unit?)? = null
    lateinit var sph : SharedPreference

    inner class ViewHolder(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
//            datafilter = data
            binding.root.setOnClickListener {
                onClick?.invoke(data[adapterPosition])
//                onClick?.invoke(datafilter[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sph = SharedPreference(holder.itemView.context)
        val df = DecimalFormat("0.00")
        val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val result = data[position]
        val kg = result?.estWeight?.div(1000)
//        val result = datafilter[position]
        val image = holder.binding.imageProfilHome
        Glide.with(holder.itemView.context)
            .load(result?.senderImage.toString())
            .into(image)
        holder.binding.nameCust.text = result?.senderName
//        holder.binding.textRange.text = df.format(distanceInKm(lat1 = result?.senderLat!!.toDouble(), lat2 = sph.fetchlatitude()!!.toDouble(), lon1 = result?.senderLong!!.toDouble(), lon2 = sph.fetchlongitude()!!.toDouble()))
        holder.binding.kg.text = rupiah.format(kg).toString().replace(",00", "").replace("Rp", "")
            .replace(",", ".")
        holder.binding.textBudget.text =
            rupiah.format(result?.estTotal).toString().replace(",00", "").replace("Rp", "")
                .replace(",", ".")
        holder.binding.senderAddress.text = result?.senderAddress
        holder.binding.recipientAddress.text = result?.recipientAddress
        holder.binding.range.text = df.format(distanceInKm(lat1 = result?.senderLat!!.toDouble(), lat2 = result?.recipientLat!!.toDouble(), lon1 = result?.senderLong!!.toDouble(), lon2 = result?.recipientLong!!.toDouble()))

        //button terima
        holder.binding.btnSend.setOnClickListener {
            accOrder(holder)
        }
    }

    override fun getItemCount(): Int {
        return data.size
//        return datafilter.size
//        return if (data.isEmpty()){
//            0
//        }else{
//            data.size
//        }
    }

    fun setData1(datas: List<OrderResponse.Data?>) {
        data.clear()
        data.addAll(datas)
//        datafilter = data
        notifyDataSetChanged()
    }

//    override fun getFilter(): Filter {
//        return object : Filter(){
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val charSearch = constraint.toString()
//                if (charSearch.isEmpty()) {
//                    datafilter = data
//                } else {
//                    val resultList = ArrayList<OrderResponse.Data?>()
//                    for (row in data) {
//                        if (row?.status(Locale.ROOT)
//                                .contains(charSearch.lowercase(Locale.ROOT))
//                        ) {
//                            resultList.add(row)
//                        }
//                    }
//                    datafilter = resultList
//                }
//                val filterResults = FilterResults()
//                filterResults.values = datafilter
//                return filterResults
//            }
//
//            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
//            }
//
//        }
//    }


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

    private fun accOrder(holder : ViewHolder){
        sph = SharedPreference(holder.itemView.context)
        val ordercode = sph.fetchordercode().toString()

        ApiMember.instanceRetrofit(holder.itemView.context).accOrder(ordercode).enqueue(object : Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                val responseBody = response.body()

                if (responseBody != null){
                    if (response.code() == 200){
                        Log.e("Auth", responseBody.toString())
                        Toast.makeText(holder.itemView.context, "Orderan berhasil diterima", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(holder.itemView.context, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}