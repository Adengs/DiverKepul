package com.codelabs.kepuldriver.model
import com.google.gson.annotations.SerializedName


data class EmailVerificationResponse(
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
        @SerializedName("driver")
        var driver: Driver?,
        @SerializedName("email_status")
        var emailStatus: EmailStatus?
    ) {
        data class Driver(
            @SerializedName("code")
            var code: String?,
            @SerializedName("email")
            var email: String?,
            @SerializedName("language")
            var language: String?
        )

        data class EmailStatus(
            @SerializedName("Data")
            var `data`: List<Data?>?,
            @SerializedName("Message")
            var message: String?,
            @SerializedName("StatusCode")
            var statusCode: Int?
        ) {
            data class Data(
                @SerializedName("Email")
                var email: Email?,
                @SerializedName("Status")
                var status: String?
            ) {
                data class Email(
                    @SerializedName("from")
                    var from: From?,
                    @SerializedName("product")
                    var product: String?,
                    @SerializedName("subject")
                    var subject: String?,
                    @SerializedName("target")
                    var target: Target?,
                    @SerializedName("type")
                    var type: String?
                ) {
                    data class From(
                        @SerializedName("email")
                        var email: String?,
                        @SerializedName("name")
                        var name: String?
                    )

                    data class Target(
                        @SerializedName("email")
                        var email: String?,
                        @SerializedName("name")
                        var name: String?
                    )
                }
            }
        }
    }
}