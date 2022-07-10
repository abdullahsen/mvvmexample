package com.asen.mvvmexample.data.repository

import com.asen.mvvmexample.model.MovieModel
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun getMoviesFromService(pageNumber: Int): Flow<List<MovieModel>>
    suspend fun getMovieById(id: Int): Flow<MovieModel>
}