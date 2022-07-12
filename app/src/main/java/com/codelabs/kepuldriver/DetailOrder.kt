package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.codelabs.kepuldriver.adapter.DetailOrderAdapter
import com.codelabs.kepuldriver.adapter.OrderAdapter
import com.codelabs.kepuldriver.databinding.ActivityDetailOrderBinding

class DetailOrder : AppCompatActivity() {
    private lateinit var binding : ActivityDetailOrderBinding
    private lateinit var adapter : DetailOrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityDetailOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        var sample = ArrayList<String>()
        sample.add("1")
        sample.add("1")
        sample.add("1")

        setAdapter(sample)

        setEvent()
    }

    private fun setAdapter(data : List<String>) {
        adapter = DetailOrderAdapter(data)
        binding.recycleViewType.adapter = adapter
        binding.recycleViewType.setHasFixedSize(true)
        adapter.onClick = {
//            startActivity(Intent(this, DetailOrder::class.java))
        }
    }
    private fun setEvent() {
        binding.btnBack.setOnClickListener {
           onBackPressed()
        }
    }
}