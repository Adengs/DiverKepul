package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class RequestUser(
    @SerializedName("email")
    var email: String?,
    @SerializedName("password")
    var password: String?
)