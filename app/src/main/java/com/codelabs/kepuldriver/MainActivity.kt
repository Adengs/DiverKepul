package com.codelabs.kepuldriver

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.codelabs.kepuldriver.fragment.HomeFragment
import com.codelabs.kepuldriver.fragment.OrderFragment
import com.codelabs.kepuldriver.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        bottom_navigate.setOnNavigationItemSelectedListener(onBottomListener)
        var fr = supportFragmentManager.beginTransaction()
        fr.add(R.id.fl_fragment, HomeFragment())
        fr.commit()
    }
    private val onBottomListener = BottomNavigationView.OnNavigationItemSelectedListener { i ->
        var selected : Fragment = HomeFragment()
        when (i.itemId){
            R.id.home -> {
                selected = HomeFragment()
            }
            R.id.order -> {
                selected = OrderFragment()
            }
            R.id.akun -> {
                selected = ProfileFragment()
            }
        }
        var fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.fl_fragment, selected)
        fr.commit()
        true
    }

}