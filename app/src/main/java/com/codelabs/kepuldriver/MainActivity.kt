package com.codelabs.kepuldriver

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.codelabs.kepuldriver.fragment.HomeFragment
import com.codelabs.kepuldriver.fragment.OrderFragment
import com.codelabs.kepuldriver.fragment.ProfileFragment
import com.codelabs.kepuldriver.helper.SharedPreference
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        bottom_navigate.setOnItemSelectedListener { i ->
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
//            fr.addToBackStack("fr - ${count++}")
            fr.commit()
            true
        }

        bottom_navigate.selectedItemId = R.id.home
//        bottom_navigate.setOnNavigationItemSelectedListener(onBottomListener)
//        var fr = supportFragmentManager.beginTransaction()
//        fr.add(R.id.fl_fragment, HomeFragment())
//        fr.commit()

        supportFragmentManager.addOnBackStackChangedListener {
            for (i in 0 until supportFragmentManager.backStackEntryCount){
                Log.d("MainActivity", supportFragmentManager.getBackStackEntryAt(i).name.toString())
            }
        }

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
        fr.addToBackStack("fr - ${count++}")
        fr.commit()
        true
    }

    override fun onBackPressed() {
        if (bottom_navigate.selectedItemId == R.id.home){
            super.onBackPressed()
        }else{
            bottom_navigate.selectedItemId = R.id.home
        }
    }
}