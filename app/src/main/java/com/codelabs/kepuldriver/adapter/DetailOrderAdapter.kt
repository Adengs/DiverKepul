package com.codelabs.kepuldriver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.databinding.ItemOrderBinding
import com.codelabs.kepuldriver.databinding.ItemTypeBinding
import com.codelabs.kepuldriver.model.DetailResponse
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class DetailOrderAdapter (val data : ArrayList<DetailResponse.Data.Detail?> = arrayListOf()) : RecyclerView.Adapter<DetailOrderAdapter.ViewHolder>() {
    var onClick : ((DetailResponse.Data.Detail?) -> Unit?)? = null
//    val data2 : List<DetailResponse.Data?> = listOf()
    inner class ViewHolder(val binding: ItemTypeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick?.invoke(data[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = data[position]
        val kg = result?.weight?.div(1000)
//        val totalkg = data2[position]?.estWeight?.div(1000)
        val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val image = holder.binding.imgJenis

        Glide.with(holder.itemView.context)
            .load(result?.productImage.toString())
            .into(image)

        holder.binding.textJenis.text = result?.productName.toString()
        holder.binding.textBerat.text = rupiah.format(kg).toString().replace(",00", "").replace("Rp", "")
            .replace(",", ".")
//        holder.binding.textBerat.text = totalkg.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setData(datas: List<DetailResponse.Data.Detail?>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }
}