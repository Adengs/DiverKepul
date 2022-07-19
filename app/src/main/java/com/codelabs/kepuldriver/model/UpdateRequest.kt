package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class UpdateRequest(
    @SerializedName("address")
    var address: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("phone")
    var phone: String?
)