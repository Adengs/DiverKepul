package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class OrderResponse(
    @SerializedName("Data")
    var `data`: List<Data?>?,
    @SerializedName("Message")
    var message: String?,
    @SerializedName("StatusCode")
    var statusCode: String?
) {
    data class Data(
        @SerializedName("est_total")
        var estTotal: Int?,
        @SerializedName("est_weight")
        var estWeight: Int?,
        @SerializedName("recipient_address")
        var recipientAddress: String?,
        @SerializedName("recipient_lat")
        var recipientLat: String?,
        @SerializedName("recipient_long")
        var recipientLong: String?,
        @SerializedName("recipient_name")
        var recipientName: String?,
        @SerializedName("reservation_code")
        var reservationCode: String?,
        @SerializedName("sender_address")
        var senderAddress: String?,
        @SerializedName("sender_image")
        var senderImage: String?,
        @SerializedName("sender_lat")
        var senderLat: String?,
        @SerializedName("sender_long")
        var senderLong: String?,
        @SerializedName("sender_name")
        var senderName: String?,
        @SerializedName("sender_phone")
        var senderPhone: String?,
        @SerializedName("status")
        var status: String?
    )
}