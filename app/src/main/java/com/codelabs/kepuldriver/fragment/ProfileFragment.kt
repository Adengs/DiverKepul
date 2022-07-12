package com.codelabs.kepuldriver.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codelabs.kepuldriver.EditProfile
import com.codelabs.kepuldriver.Notification
import com.codelabs.kepuldriver.R
import com.codelabs.kepuldriver.SplashScreen
import com.codelabs.kepuldriver.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        context ?: binding.root

        setEvent()
        return binding.root
    }
    private fun setEvent(){
        binding.nextButtonEditProfil.setOnClickListener {
            startActivity(Intent(view?.context, EditProfile::class.java))
        }
        binding.nextButtonNotif.setOnClickListener {
            startActivity(Intent(view?.context, Notification::class.java))
        }
        binding.layLogout.setOnClickListener {
            startActivity(Intent(view?.context, SplashScreen::class.java))
        }
    }
}