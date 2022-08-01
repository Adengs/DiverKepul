package com.codelabs.kepuldriver.adapter

import android.graphics.Path
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codelabs.kepuldriver.OrderDetail
import com.codelabs.kepuldriver.databinding.ItemFotoOrderBinding
import com.codelabs.kepuldriver.databinding.ItemTypeValidationBinding
import com.codelabs.kepuldriver.model.DetailResponse

class FotoVatidationAdapter (val data : ArrayList<DetailResponse.Data.Detail?> = arrayListOf()) : RecyclerView.Adapter<FotoVatidationAdapter.ViewHolder>() {
    var onClick : ((DetailResponse.Data.Detail?) -> Unit?)? = null
    inner class ViewHolder(val binding: ItemFotoOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick?.invoke(data[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFotoOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = data[position]
        val image = holder.binding.fotoOrder

            Glide.with(holder.itemView.context)
                .load(result?.imagePath.toString())
                .into(image)

        holder.binding.deleteFoto.setOnClickListener {
            data.removeAt(position)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setFoto(datas: List<DetailResponse.Data.Detail?>) {
        data.clear()
        data.addAll(datas)
        notifyDataSetChanged()
    }
}