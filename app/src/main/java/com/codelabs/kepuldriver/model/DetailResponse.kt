package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class DetailResponse(
    @SerializedName("Data")
    var `data`: Data?,
    @SerializedName("Message")
    var message: String?,
    @SerializedName("StatusCode")
    var statusCode: String?
) {
    data class Data(
        @SerializedName("detail")
        var detail: List<Detail?>?,
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
        @SerializedName("shipping_date")
        var shippingDate: String?,
        @SerializedName("status")
        var status: String?
    ) {
        data class Detail(
            @SerializedName("category_name")
            var categoryName: String?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("currency")
            var currency: String?,
            @SerializedName("is_special_deals")
            var isSpecialDeals: String?,
            @SerializedName("notes")
            var notes: Any?,
            @SerializedName("ppn")
            var ppn: Int?,
            @SerializedName("ppn_price")
            var ppnPrice: Int?,
            @SerializedName("price")
            var price: Int?,
            @SerializedName("price_before_discount")
            var priceBeforeDiscount: Int?,
            @SerializedName("price_before_ppn")
            var priceBeforePpn: Int?,
            @SerializedName("product_code")
            var productCode: String?,
            @SerializedName("product_image")
            var productImage: String?,
            @SerializedName("product_image_driver")
            var productImageDriver: Any?,
            @SerializedName("product_name")
            var productName: String?,
            @SerializedName("quantity")
            var quantity: Int?,
            @SerializedName("quantity_by_customer")
            var quantityByCustomer: Int?,
            @SerializedName("quantity_by_driver")
            var quantityByDriver: Int?,
            @SerializedName("quantity_by_warehouse")
            var quantityByWarehouse: Int?,
            @SerializedName("reservation_detail_id")
            var reservationDetailId: Int?,
            @SerializedName("reservation_id")
            var reservationId: Int?,
            @SerializedName("saving_price")
            var savingPrice: Int?,
            @SerializedName("subtotal")
            var subtotal: Int?,
            @SerializedName("unit")
            var unit: String?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("warehouse_note")
            var warehouseNote: String?,
            @SerializedName("weight")
            var weight: Int?,
            @SerializedName("weight_unit")
            var weightUnit: String?
        )
    }
}