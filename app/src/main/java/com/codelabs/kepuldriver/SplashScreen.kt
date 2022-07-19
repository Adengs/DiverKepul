package com.codelabs.kepuldriver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.codelabs.kepuldriver.api.ApiConfig
import com.codelabs.kepuldriver.helper.SharedPreference
import com.codelabs.kepuldriver.model.TokenRequest
import com.codelabs.kepuldriver.model.TokenResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashScreen : AppCompatActivity() {
    lateinit var sph : SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        Handler().postDelayed({
            hitToken()
        }, 3000)
    }

    private fun hitToken() {
        sph = SharedPreference(this)
        val intent = Intent(this, Login::class.java)

        ApiConfig.instanceRetrofit(this).hitToken(TokenRequest(user = "codelabs-android", secret = "yju1Z7V5II63hoitOx1QsLvj9clpL07lT978cCKn", version = "1.0.0"))
            .enqueue(object : Callback<TokenResponse>{
                override fun onResponse(
                    call: Call<TokenResponse>,
                    response: Response<TokenResponse>
                ) {
                    val tokenResponse = response.body()

                    if (tokenResponse != null){
                        sph.saveAuthToken(tokenResponse.data?.token.toString())
                        Log.e("Auth1", tokenResponse.toString())
                        startActivity(intent)
                    }else{
                        Log.e("Auth2", response.errorBody()!!.string())
                    }
                }


                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                    t.printStackTrace()
                }

            })

    }
}