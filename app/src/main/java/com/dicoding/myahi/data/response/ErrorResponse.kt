package com.dicoding.myahi.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error")
    val isError: Boolean? = null,

    @SerializedName("message")
    val errorMessage: String? = null
)

