package com.dicoding.myahi.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("UserLoginDetails")
    val userLoginDetails: UserLoginDetails? = null,

    @field:SerializedName("error")
    val isError: Boolean? = null,

    @field:SerializedName("message")
    val responseMessage: String? = null
)

data class UserLoginDetails(
    @field:SerializedName("name")
    val userName: String? = null,

    @field:SerializedName("userId")
    val id: String? = null,

    @field:SerializedName("token")
    val authToken: String? = null
)
