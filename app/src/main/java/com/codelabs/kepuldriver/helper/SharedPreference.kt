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
}