package com.example.sub1int2.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)