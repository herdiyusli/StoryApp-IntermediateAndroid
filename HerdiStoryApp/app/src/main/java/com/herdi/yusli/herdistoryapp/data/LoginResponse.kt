package com.herdi.yusli.herdistoryapp.data

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @field:SerializedName("loginResult")
    val loginResult: User,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class User(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("token")
    val token: String
)
