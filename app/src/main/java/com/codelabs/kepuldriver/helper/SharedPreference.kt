package com.codelabs.kepuldriver.helper

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast

class SharedPreference(context: Context) {
    private val myPref = "My_Pref"
    val sharedPreference: SharedPreferences

    init {
        sharedPreference = context.getSharedPreferences(myPref, Context.MODE_PRIVATE)
    }

    companion object {
        const val USER_TOKEN = "token"
        const val LOGIN ="login"
        const val PASSWORD = "password"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val ORDERCODE = "ordercode"
        const val EMAIL = "email"
        const val VERIFCODE = "verification_code"
        const val READYSTATUS = "ready"
        const val ORDERCODEA = "ordercode"
    }


    //function save token
    fun saveAuthToken(token: String){
        sharedPreference.edit().putString(USER_TOKEN, token).apply()
    }

    //function fetch token
    fun fetchAuthToken(): String? {
        return  sharedPreference.getString(USER_TOKEN, "")
    }

    //boolean put
    fun put(login: Boolean){
        sharedPreference.edit()
            .putBoolean(LOGIN, login)
            .apply()
    }

    //boolean get
    fun getBoolean() : Boolean{
        return sharedPreference.getBoolean(LOGIN, false)
    }

    //function save password
    fun savePassword(password: String){
        sharedPreference.edit().putString(PASSWORD, password).apply()
    }

    //function fetch password
    fun fetchPassword(): String? {
        return  sharedPreference.getString(PASSWORD, "")
    }

    //function save latitude
    fun savelatitude(lat: String){
        sharedPreference.edit().putString(LATITUDE, lat).apply()
    }

    //function fetch latitude
    fun fetchlatitude(): String? {
        return  sharedPreference.getString(LATITUDE, "")
    }

    //function save longitude
    fun savelongitude(long: String){
        sharedPreference.edit().putString(LONGITUDE, long).apply()
    }

    //function fetch longitude
    fun fetchlongitude(): String? {
        return  sharedPreference.getString(LONGITUDE, "")
    }

    //function save ordercode
    fun saveordercode(code: String){
        sharedPreference.edit().putString(ORDERCODE, code).apply()
    }

    //function fetch ordercode
    fun fetchordercode(): String? {
        return  sharedPreference.getString(ORDERCODE, "")
    }

    //function save email
    fun saveemail(email: String){
        sharedPreference.edit().putString(EMAIL, email).apply()
    }

    //function fetch email
    fun fetchemail(): String? {
        return  sharedPreference.getString(EMAIL, "")
    }

    //function save code
    fun savecode(code: String){
        sharedPreference.edit().putString(VERIFCODE, code).apply()
    }

    //function fetch code
    fun fetchcodee(): String? {
        return  sharedPreference.getString(VERIFCODE, "")
    }

    //boolean put ready
    fun ready(ready: Boolean){
        sharedPreference.edit()
            .putBoolean(READYSTATUS, ready)
            .apply()
    }

    //boolean get ready
    fun getReady() : Boolean{
        return sharedPreference.getBoolean(READYSTATUS, true)
    }

    //function save ordercode aktif
    fun saveordercodeaktif(code: String){
        sharedPreference.edit().putString(ORDERCODEA, code).apply()
    }

    //function fetch ordercode aktif
    fun fetchordercodeaktif(): String? {
        return  sharedPreference.getString(ORDERCODEA, "")
    }

}