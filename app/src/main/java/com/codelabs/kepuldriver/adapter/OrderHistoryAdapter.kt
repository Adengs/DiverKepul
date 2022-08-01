package com.codelabs.kepuldriver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.databinding.ItemOrderBinding
import com.codelabs.kepuldriver.databinding.ItemOrderHistoryBinding
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.OrderResponse
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderHistoryAdapter (val data : ArrayList<OrderResponse.Data?> = arrayListOf()) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    var onClick : ((OrderResponse.Data?) -> Unit?)? = null
    lateinit var sph : SharedPreference
    inner class ViewHolder(val binding: ItemOrderHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick?.invoke(data[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sph = SharedPreference(holder.itemView.context)
        val df = DecimalFormat("0.0")
        val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val result = data[position]
        val totalkg = result?.estWeight?.div(1000)
        val image = holder.binding.imageProfilHome


        holder.binding.noOrder.text = result?.reservationCode
        Glide.with(holder.itemView.context)
            .load(result?.senderImage.toString())
            .into(image)
        holder.binding.nameCust.text = result?.senderName
//        holder.binding.textRange.text = df.format(distanceInKm(lat1 = result?.senderLat!!.toDouble(), lat2 = sph.fetchlatitude()!!.toDouble(), lon1 = result?.senderLong!!.toDouble(), lon2 = sph.fetchlongitude()!!.toDouble()))
        holder.binding.textBerat.text = totalkg.toString()
        holder.binding.textBudget.text =
            rupiah.format(result?.estTotal).toString().replace(",00", "").replace("Rp", "")
                .replace(",", ".")
        holder.binding.senderAddress.text = result?.senderAddress
        holder.binding.recipientAddress.text = result?.recipientAddress
        holder.binding.range.text = df.format(distanceInKm(lat1 = result?.senderLat!!.toDouble(), lat2 = result?.recipientLat!!.toDouble(), lon1 = result?.senderLong!!.toDouble(), lon2 = result?.recipientLong!!.toDouble()))

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData3(datas: List<OrderResponse.Data?>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
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