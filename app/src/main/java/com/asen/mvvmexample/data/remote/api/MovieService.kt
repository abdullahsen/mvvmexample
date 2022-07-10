package com.asen.mvvmexample.data.remote.api

import com.asen.mvvmexample.data.remote.response.MoviesResponse
import com.asen.mvvmexample.util.Resource
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int
    ): MoviesResponse
}