package com.codelabs.kepuldriver.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.OrderDetail
import com.codelabs.kepuldriver.databinding.ActivityDetailOrderBinding
import com.codelabs.kepuldriver.databinding.ItemOrderBinding
import com.codelabs.kepuldriver.databinding.ItemTypeValidationBinding
import com.codelabs.kepuldriver.model.DetailResponse
import com.codelabs.kepuldriver.model.OrderResponse
import java.lang.String
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class TypeValidationAdapter (val onChange : OrderDetail.OnChange, val data : ArrayList<DetailResponse.Data.Detail?> = arrayListOf()) : RecyclerView.Adapter<TypeValidationAdapter.ViewHolder>() {
    var onClick : ((DetailResponse.Data.Detail?) -> Unit?)? = null
    var value = ""
    inner class ViewHolder(val binding: ItemTypeValidationBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick?.invoke(data[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTypeValidationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = data[position]
        val df = DecimalFormat("0.0")
        val kg = result?.weight?.div(1000)
        val rupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val image = holder.binding.imgJenis

        holder.binding.imgDelete.setOnClickListener {  }
        Glide.with(holder.itemView.context)
            .load(result?.productImage.toString())
            .into(image)

        holder.binding.textJenis.text = result?.productName.toString()
        holder.binding.textTimbangan.text = kg.toString()
        value = result?.productCode.toString()
        Log.e("cek" , value)
//        holder.binding.textBerat.text = df.format(kg)

//        if (result?.quantity!! == 0){
//            holder.binding.btnMinus.isEnabled = false
//        }
//        if (result.weight!! == 0){
//            holder.binding.btnMinus.isEnabled = false
//        }else {
            if (result?.unit == "Unit"){
                holder.binding.textBeratKg.text = "Unit"
                holder.binding.textBerat.text = result.quantity.toString()
                holder.binding.btnPlus.setOnClickListener {
                    val sum = holder.binding.textBerat.text.toString().toInt()
                    val price = result.subtotal!!
                    val pricePer = result.price!!
                    val hitung = sum + 1
                    val hitungprice = price + pricePer

                    holder.binding.btnMinus.isEnabled = true
                    holder.binding.textBerat.text = hitung.toString()
                    result.quantity = hitung
                    result.subtotal = hitungprice
                    onChange.onChangeListener()
                }
                holder.binding.btnMinus.setOnClickListener {
                    val sum = holder.binding.textBerat.text.toString().toInt()
                    val price = result.subtotal!!
                    val pricePer = result.price!!

                    if (sum == 0){
                        holder.binding.btnMinus.isEnabled = false
                    }
                    else {
                        val hitung = sum - 1
                        val hitungprice = price - pricePer
                        holder.binding.textBerat.text = hitung.toString()
                        result.quantity = hitung
                        result.subtotal = hitungprice
                        onChange.onChangeListener()}
                }
            }
            if (result?.unit == "Kilogram") {
                holder.binding.textBerat.text = kg.toString()
                holder.binding.btnPlus.setOnClickListener {
                    val sum = holder.binding.textBerat.text.toString().toInt()
                    val tambah = 1000
                    val plus = tambah.div(1000)
                    val price = result.subtotal!!
                    val pricePer = result.price!!
                    val hitung = sum + plus
                    val hitungprice = price + pricePer

                    holder.binding.btnMinus.isEnabled = true
                    holder.binding.textBerat.text = hitung.toString()
                    result.weight = hitung.times(1000)
                    result.subtotal = hitungprice
                    onChange.onChangeListener()
                }
                holder.binding.btnMinus.setOnClickListener {
                    val sum2 = holder.binding.textBerat.text.toString().toInt()
                    val kurang = 1000
                    val min = kurang.div(1000)
                    val price = result.subtotal!!
                    val pricePer = result.price!!

                    if (sum2 == 0){
                        holder.binding.btnMinus.isEnabled = false
                    }
                    else {
                        val hitung2 = sum2 - min
                        val hitungprice = price - pricePer

                        holder.binding.textBerat.text = hitung2.toString()
                        result.weight = hitung2.times(1000)
                        result.subtotal = hitungprice
                        onChange.onChangeListener()}

                }

//            }
        }

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