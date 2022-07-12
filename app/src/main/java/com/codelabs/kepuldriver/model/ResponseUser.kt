package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class ResponseUser(
    @SerializedName("Data")
    var `data`: Data?,
    @SerializedName("Message")
    var message: String?,
    @SerializedName("StatusCode")
    var statusCode: String?
) {
    data class Data(
        @SerializedName("status")
        var status: String?,
        @SerializedName("token")
        var token: String?
    )
}