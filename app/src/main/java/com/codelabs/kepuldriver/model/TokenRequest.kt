package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class TokenRequest(
    @SerializedName("Secret")
    var secret: String?,
    @SerializedName("User")
    var user: String?,
    @SerializedName("Version")
    var version: String?
)