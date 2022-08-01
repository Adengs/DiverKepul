package com.codelabs.kepuldriver.model
import android.net.Uri
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
        @SerializedName("reservation_type")
        var reservationType: String?,
        @SerializedName("reservation_type_color")
        var reservationTypeColor: String?,
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
            var imagePath : Uri?,
            @SerializedName("category_name")
            var categoryName: String? = null,
            @SerializedName("created_at")
            var createdAt: String? = null,
            @SerializedName("currency")
            var currency: String? = null,
            @SerializedName("is_special_deals")
            var isSpecialDeals: String? = null,
            @SerializedName("notes")
            var notes: Any? = null,
            @SerializedName("ppn")
            var ppn: Int? = null,
            @SerializedName("ppn_price")
            var ppnPrice: Int? = null,
            @SerializedName("price")
            var price: Int? = null,
            @SerializedName("price_before_discount")
            var priceBeforeDiscount: Int? = null,
            @SerializedName("price_before_ppn")
            var priceBeforePpn: Int? = null,
            @SerializedName("product_code")
            var productCode: String? = null,
            @SerializedName("product_image")
            var productImage: String? = null,
            @SerializedName("product_image_driver")
            var productImageDriver: Any? = null,
            @SerializedName("product_name")
            var productName: String? = null,
            @SerializedName("quantity")
            var quantity: Int? = null,
            @SerializedName("quantity_by_customer")
            var quantityByCustomer: Int? = null,
            @SerializedName("quantity_by_driver")
            var quantityByDriver: Int? = null,
            @SerializedName("quantity_by_warehouse")
            var quantityByWarehouse: Int? = null,
            @SerializedName("reservation_detail_id")
            var reservationDetailId: Int? = null,
            @SerializedName("reservation_id")
            var reservationId: Int? = null,
            @SerializedName("saving_price")
            var savingPrice: Int? = null,
            @SerializedName("subtotal")
            var subtotal: Int ? = null,
            @SerializedName("unit")
            var unit: String? = null,
            @SerializedName("updated_at")
            var updatedAt: String? = null,
            @SerializedName("warehouse_note")
            var warehouseNote: String? = null,
            @SerializedName("weight")
            var weight: Int? = null,
            @SerializedName("weight_unit")
            var weightUnit: String? = null
        )
    }
}