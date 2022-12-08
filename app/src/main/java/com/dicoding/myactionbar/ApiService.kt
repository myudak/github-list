package com.dicoding.myactionbar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    fun getUsers(
        @Query("q") id : String
    ): Call<GithubResponse>

    @GET("{name}/{method}")
    fun getFollowUsers(
        @Path("name") name: String,
        @Path("method") method: String
    ): Call<List<GithubFollowersResponseItem>>

    @GET("{name}")
    fun getDetailUsers(
        @Path("name") name: String
    ): Call<GithubDetailResponse>
}