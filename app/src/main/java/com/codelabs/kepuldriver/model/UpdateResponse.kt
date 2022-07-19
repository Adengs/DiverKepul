package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class UpdateResponse(
    @SerializedName("Data")
    var `data`: Data?,
    @SerializedName("Message")
    var message: String?,
    @SerializedName("StatusCode")
    var statusCode: String?
) {
    data class Data(
        @SerializedName("company_parent_code")
        var companyParentCode: String?,
        @SerializedName("created_at")
        var createdAt: String?,
        @SerializedName("created_by")
        var createdBy: String?,
        @SerializedName("deleted_at")
        var deletedAt: Any?,
        @SerializedName("driver_id")
        var driverId: Int?,
        @SerializedName("driver_profile_id")
        var driverProfileId: Int?,
        @SerializedName("email")
        var email: String?,
        @SerializedName("email_verified_at")
        var emailVerifiedAt: Any?,
        @SerializedName("profile")
        var profile: Profile?,
        @SerializedName("ready_status")
        var readyStatus: String?,
        @SerializedName("remember_token")
        var rememberToken: Any?,
        @SerializedName("status")
        var status: String?,
        @SerializedName("transportation")
        var transportation: Transportation?,
        @SerializedName("transportation_id")
        var transportationId: Int?,
        @SerializedName("updated_at")
        var updatedAt: String?,
        @SerializedName("updated_by")
        var updatedBy: String?
    ) {
        data class Profile(
            @SerializedName("address")
            var address: String?,
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("deleted_at")
            var deletedAt: Any?,
            @SerializedName("driver_profile_id")
            var driverProfileId: Int?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("phone")
            var phone: String?,
            @SerializedName("plate_number")
            var plateNumber: String?,
            @SerializedName("updated_at")
            var updatedAt: String?
        )

        data class Transportation(
            @SerializedName("created_at")
            var createdAt: String?,
            @SerializedName("created_by")
            var createdBy: String?,
            @SerializedName("deleted_at")
            var deletedAt: Any?,
            @SerializedName("description")
            var description: String?,
            @SerializedName("image")
            var image: String?,
            @SerializedName("name")
            var name: String?,
            @SerializedName("transportation_id")
            var transportationId: Int?,
            @SerializedName("updated_at")
            var updatedAt: String?,
            @SerializedName("updated_by")
            var updatedBy: String?
        )
    }
}