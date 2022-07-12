package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class TokenResponse(
    @SerializedName("Data")
    var `data`: Data?,
    @SerializedName("Message")
    var message: String?,
    @SerializedName("Service")
    var service: String?,
    @SerializedName("StatusCode")
    var statusCode: String?
) {
    data class Data(
        @SerializedName("token")
        var token: String?,
        @SerializedName("verified")
        var verified: String?
    )
}