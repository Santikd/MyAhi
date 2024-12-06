package com.dicoding.myahi.data.response

import com.google.gson.annotations.SerializedName

data class RegistrationResult(
    @SerializedName("error")
    val isError: Boolean? = null,

    @SerializedName("message")
    val responseMessage: String? = null
)
