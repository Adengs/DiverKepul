package com.codelabs.kepuldriver.model

import com.google.gson.annotations.SerializedName

data class PasswordRequest(
    @SerializedName("password")
    var password: String?,
)