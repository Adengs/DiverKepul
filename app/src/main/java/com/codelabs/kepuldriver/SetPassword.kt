package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.codelabs.kepuldriver.databinding.ActivityEmailVerificationBinding
import com.codelabs.kepuldriver.databinding.ActivitySetPasswordBinding

class SetPassword : AppCompatActivity() {
    private lateinit var binding : ActivitySetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivitySetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setEvent()
    }

    private fun setEvent() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, EmailVerification::class.java))
        }
        binding.btnSubmit.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }
}