package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.codelabs.kepuldriver.databinding.ActivityChangePasswordBinding
import com.codelabs.kepuldriver.databinding.ActivityEmailVerificationBinding

class EmailVerification : AppCompatActivity() {
    private lateinit var binding : ActivityEmailVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, ChangePassword::class.java))
        }
        binding.btnSend.setOnClickListener {
            startActivity(Intent(this, SetPassword::class.java))
        }
        binding.resendCode.setOnClickListener {

        }
    }
}