package com.herdi.yusli.herdistoryapp.data

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface RetrofitApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerRequest(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginRequest(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token: String,
        @Query("location") location: Int? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddStoryResponse>

    @GET("stories")
    fun getStoryMapLocation(
        @Header("Authorization") Authorization: String,
        @Query("location") location: Int? = null
    ): Call<StoryResponse>

}