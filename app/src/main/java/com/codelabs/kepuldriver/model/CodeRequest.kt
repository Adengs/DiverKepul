package com.codelabs.kepuldriver.model

import com.google.gson.annotations.SerializedName

data class CodeRequest (
    @SerializedName("code")
    var Code: String?,
        )