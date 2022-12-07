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

    @GET("{name}/followers")
    fun getFollowers(
        @Path("name") name: String
    ): Call<GithubFollowersResponse>
}