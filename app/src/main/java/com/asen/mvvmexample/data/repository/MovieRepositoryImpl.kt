package com.asen.mvvmexample.data.repository

import android.util.Log
import com.asen.mvvmexample.data.local.dao.MovieDao
import com.asen.mvvmexample.data.remote.api.MovieService
import com.asen.mvvmexample.data.remote.response.MoviesResponse
import com.asen.mvvmexample.data.repository.mapper.MovieEntityToMovieModelMapper
import com.asen.mvvmexample.data.repository.mapper.MovieResponseToMovieEntityMapper
import com.asen.mvvmexample.model.MovieModel
import com.asen.mvvmexample.util.Resource
import com.asen.mvvmexample.util.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val movieDao: MovieDao,
    private val movieService: MovieService,
    private val movieEntityToMovieModelMapper: MovieEntityToMovieModelMapper,
    private val movieResponseToMovieEntityMapper: MovieResponseToMovieEntityMapper
) : MovieRepository {

    override suspend fun getMoviesFromService(pageNumber: Int): Flow<List<MovieModel>> {
        return when (val response = safeApiCall { movieService.getMovies(pageNumber) }) {
            is Resource.Error -> handleFailedResponse(response)
            is Resource.Success -> handleSuccessResponse(response)
        }
    }

    override suspend fun getMovieById(id: Int): Flow<MovieModel> {
        return movieDao.getMovieById(id).map { movieEntity ->
            movieEntityToMovieModelMapper.map(movieEntity)
        }
    }

    private fun getMoviesFromDatabase(): Flow<List<MovieModel>> {
        return movieDao.loadMovies().map { movies ->
            movies.map { movieEntityToMovieModelMapper.map(it) }
        }
    }

    private suspend fun handleSuccessResponse(
        response: Resource.Success<MoviesResponse>
    ): Flow<List<MovieModel>> {
        val movieList = response.data.results.map {
            movieResponseToMovieEntityMapper.map(it)
        }
        movieDao.insertMovies(*movieList.toTypedArray())
        return flow { emit(movieList.map { movieEntityToMovieModelMapper.map(it) }) }
    }

    private fun handleFailedResponse(response: Resource.Error): Flow<List<MovieModel>> {
        //rooLog.d(API_ERROR_LOG_TAG, response.exception.message.toString())
        return getMoviesFromDatabase()
    }

    private companion object {
        private const val API_ERROR_LOG_TAG = "API_ERROR"
    }

}