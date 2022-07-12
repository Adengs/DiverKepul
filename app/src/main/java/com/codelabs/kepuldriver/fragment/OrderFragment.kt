package com.codelabs.kepuldriver.fragment

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.codelabs.kepuldriver.OrderDetail
import com.codelabs.kepuldriver.R
import com.codelabs.kepuldriver.adapter.OrderAktifAdapter
import com.codelabs.kepuldriver.adapter.OrderHistoryAdapter
import com.codelabs.kepuldriver.databinding.FragmentOrderBinding

class OrderFragment : Fragment() {
    private lateinit var binding: FragmentOrderBinding
    private lateinit var adapter1: OrderAktifAdapter
    private lateinit var adapter2: OrderHistoryAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
        context ?: return binding.root

        var sample = ArrayList<String>()
        sample.add("1")
        sample.add("1")

        setAdapter1(sample)
        binding.recycleViewOrderAktif.visibility = View.VISIBLE
        binding.materialButtonAktif.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))
        binding.materialButtonHistory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_kepul)))

        onChecked()
        return binding.root
    }

    private fun onChecked() {
        var sample = ArrayList<String>()
        sample.add("1")
        sample.add("1")

        binding.toogleGroup.addOnButtonCheckedListener { toogle, checkedId, isChecked ->
//            var selected : Fragment = OrderAktifFragment()
            if (isChecked) {
                when (checkedId) {
                    R.id.material_button_aktif -> {
                        setAdapter1(sample)
                        binding.recycleViewOrderHistory.visibility = View.GONE
                        binding.recycleViewOrderAktif.visibility = View.VISIBLE
                        binding.materialButtonAktif.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))
                        binding.materialButtonHistory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_kepul)))
                    }
                    R.id.material_button_history -> {
                        setAdapter2(sample)
                        binding.recycleViewOrderAktif.visibility = View.GONE
                        binding.recycleViewOrderHistory.visibility = View.VISIBLE
                        binding.materialButtonAktif.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.blue_kepul)))
                        binding.materialButtonHistory.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white)))
                    }
                }
            } else {
                if (toogle.checkedButtonId == View.NO_ID) {
                    Toast.makeText(view?.context, "Tidak ada yang dipilih", Toast.LENGTH_LONG)
                        .show()
                    binding.recycleViewOrderAktif.visibility = View.GONE
                    binding.recycleViewOrderHistory.visibility = View.GONE
                }
            }
        }
    }

    private fun setAdapter1(data: List<String>) {
        adapter1 = OrderAktifAdapter(data)
        binding.recycleViewOrderAktif.adapter = adapter1
        binding.recycleViewOrderAktif.setHasFixedSize(true)
        adapter1.onClick = {
            startActivity(Intent(view?.context, OrderDetail::class.java))
        }
    }

    private fun setAdapter2(data: List<String>) {
        adapter2 = OrderHistoryAdapter(data)
        binding.recycleViewOrderHistory.adapter = adapter2
        binding.recycleViewOrderHistory.setHasFixedSize(true)
        adapter2.onClick = {
//            startActivity(Intent(view?.context, OrderDetail::class.java))
        }
    }
}
