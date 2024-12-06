package com.dicoding.myahi.data.preferences

data class UserData(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
