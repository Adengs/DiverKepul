package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.codelabs.kepuldriver.adapter.FotoVatidationAdapter
import com.codelabs.kepuldriver.adapter.OrderAdapter
import com.codelabs.kepuldriver.adapter.TypeValidationAdapter
import com.codelabs.kepuldriver.databinding.ActivityOrderDetailBinding
import com.codelabs.kepuldriver.fragment.OrderFragment

class OrderDetail : AppCompatActivity() {
    private lateinit var binding : ActivityOrderDetailBinding
    private lateinit var adapter1 : TypeValidationAdapter
    private lateinit var adapter2 : FotoVatidationAdapter
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
    }

    private fun setAdapter1(data : List<String>) {
        adapter1 = TypeValidationAdapter(data)
        binding.recycleViewType.adapter = adapter1
        binding.recycleViewType.setHasFixedSize(true)
        adapter1.onClick = {
//            startActivity(Intent(this, DetailOrder::class.java))
        }
    }

    private fun setAdapter2(data : List<String>) {
        adapter2 = FotoVatidationAdapter(data)
        binding.recycleViewFoto.adapter = adapter2
        binding.recycleViewFoto.setHasFixedSize(true)
        adapter1.onClick = {
//            startActivity(Intent(this, DetailOrder::class.java))
        }
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}