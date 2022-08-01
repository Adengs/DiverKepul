package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class ProcessOrderRequest(
    @SerializedName("price")
    var price: String?,
    @SerializedName("product")
    var product: List<Product?>?
) {
    data class Product(
        @SerializedName("code")
        var code: String?,
        @SerializedName("image")
        var image: String?,
        @SerializedName("quantity")
        var quantity: String?
    )
}