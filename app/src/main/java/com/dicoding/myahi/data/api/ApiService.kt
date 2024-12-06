package com.dicoding.myahi.data.api

import com.dicoding.myahi.data.response.DetailStoriesResponse
import com.dicoding.myahi.data.response.ErrorResponse
import com.dicoding.myahi.data.response.LoginResponse
import com.dicoding.myahi.data.response.RegistrationResult
import com.dicoding.myahi.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegistrationResult

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse
    @GET("stories")
    suspend fun getStories(): StoryResponse
    @GET("stories/{id}")
    suspend fun getDetailStory(
        @Path("id") id: String?
    ): DetailStoriesResponse
    @Multipart
    @POST("stories")
    suspend fun addStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): ErrorResponse
}