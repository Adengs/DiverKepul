package com.codelabs.kepuldriver.helper

import android.content.Context
import android.content.SharedPreferences

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
        const val LATITUDESEND = "latitude"
        const val LONGITUDESEND = "longitude"
        const val LATITUDEREC = "latitude"
        const val LONGITUDEREC = "longitude"
        const val ORDERCODE = "ordercode"
        const val CLICKED = "acceptorder"
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

    //function save latitude sender
    fun savelatitudesend(lat: String){
        sharedPreference.edit().putString(LATITUDESEND, lat).apply()
    }

    //function fetch latitude sender
    fun fetchlatitudesend(): String? {
        return  sharedPreference.getString(LATITUDESEND, "")
    }

    //function save longitude sender
    fun savelongitudesend(long: String){
        sharedPreference.edit().putString(LONGITUDESEND, long).apply()
    }

    //function fetch longitude sender
    fun fetchlongitudesend(): String? {
        return  sharedPreference.getString(LONGITUDE, "")
    }

    //function save latitude recipier
    fun savelatituderec(lat: String){
        sharedPreference.edit().putString(LATITUDEREC, lat).apply()
    }

    //function fetch latitude recipier
    fun fetchlatituderec(): String? {
        return  sharedPreference.getString(LATITUDEREC, "")
    }

    //function save longitude recipier
    fun savelongituderec(long: String){
        sharedPreference.edit().putString(LONGITUDEREC, long).apply()
    }

    //function fetch longitude recipier
    fun fetchlongituderec(): String? {
        return  sharedPreference.getString(LONGITUDEREC, "")
    }

    //function save ordercode
    fun saveordercode(code: String){
        sharedPreference.edit().putString(ORDERCODE, code).apply()
    }

    //function fetch longitude recipier
    fun fetchordercode(): String? {
        return  sharedPreference.getString(ORDERCODE, "")
    }

    //boolean put order acc
    fun putorderacc(acc: Boolean){
        sharedPreference.edit()
            .putBoolean(CLICKED, acc)
            .apply()
    }

    //boolean get order acc
    fun getorderacc() : Boolean{
        return sharedPreference.getBoolean(CLICKED, false)
    }
}