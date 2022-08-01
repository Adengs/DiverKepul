package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class EmailRequest(
    @SerializedName("email")
    var email: String?
)