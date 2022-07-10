package com.asen.mvvmexample.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.asen.mvvmexample.MainDispatcherRule
import com.asen.mvvmexample.data.local.dao.MovieDao
import com.asen.mvvmexample.data.local.entity.MovieEntity
import com.asen.mvvmexample.data.remote.api.MovieService
import com.asen.mvvmexample.data.remote.response.MoviesResponse
import com.asen.mvvmexample.data.repository.mapper.MovieEntityToMovieModelMapper
import com.asen.mvvmexample.data.repository.mapper.MovieResponseToMovieEntityMapper
import com.asen.mvvmexample.model.MovieModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieRepository: MovieRepositoryImpl
    private val movieDao = mock<MovieDao>()
    private val movieService = mock<MovieService>()
    private val movieEntityToMovieModelMapper = MovieEntityToMovieModelMapper()
    private val movieResponseToMovieEntityMapper = MovieResponseToMovieEntityMapper()

    @Before
    internal fun setUp() {
        movieRepository = MovieRepositoryImpl(
            movieDao,
            movieService,
            movieEntityToMovieModelMapper,
            movieResponseToMovieEntityMapper
        )
    }

    @Test
    fun `verify cached movie item is loaded when given movie id is exist`() = runTest {
        whenever(movieDao.getMovieById(any())).thenReturn(flow { emit(MOVIE_ENTITY_1) })
        val actual = movieRepository.getMovieById(2).first()
        assertEquals(MOVIE_MODEL_1, actual)
    }

    @Test
    fun `verify cached movie items are loaded when network request fails`() = runTest {
        whenever(movieDao.loadMovies()).thenReturn(flow { emit(listOf(MOVIE_ENTITY_1)) })
        whenever(movieService.getMovies(any())).thenThrow(RuntimeException())
        val actual = movieRepository.getMoviesFromService(1).first()
        assertEquals(listOf(MOVIE_MODEL_1), actual)
    }

    @Test
    fun `verify remote movie items are loaded when network request succeed`() = runTest {
        whenever(movieService.getMovies(any())).thenReturn(MOVIE_RESPONSE)
        val actual = movieRepository.getMoviesFromService(1).first()
        assertEquals(listOf(MOVIE_MODEL_1), actual)
    }


    private companion object {
        val MOVIE_ENTITY_1 = MovieEntity(
            id = 453395,
            title = "Doctor Strange in the Multiverse of Madness",
            description = "Doctor Strange, with the help of mystical allies both old and new, traverses the mind-bending and dangerous alternate realities of the Multiverse to confront a mysterious new adversary.",
            imageUrl = "/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg",
            releaseDate = "2022-05-04",
            voteAverage = 7.5
        )

        val MOVIE_MODEL_1 = MovieModel(
            id = 453395,
            title = "Doctor Strange in the Multiverse of Madness",
            description = "Doctor Strange, with the help of mystical allies both old and new, traverses the mind-bending and dangerous alternate realities of the Multiverse to confront a mysterious new adversary.",
            image = "/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg",
            releaseDate = "2022-05-04",
            voteAverage = 7.5
        )

        val MOVIE_RESPONSE = MoviesResponse(
            page = 1,
            totalPages = 1,
            totalResults = 1,
            results = listOf(
                MoviesResponse.Result(
                    adult = false,
                    backdropPath = "/wcKFYIiVDvRURrzglV9kGu7fpfY.jpg",
                    genreIds = listOf(12),
                    id = 453395,
                    originalLanguage = "en",
                    originalTitle = "Doctor Strange in the Multiverse of Madness",
                    overview = "Doctor Strange, with the help of mystical allies both old and new, traverses the mind-bending and dangerous alternate realities of the Multiverse to confront a mysterious new adversary.",
                    popularity = 6343.173,
                    posterPath = "/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg",
                    releaseDate = "2022-05-04",
                    title = "Doctor Strange in the Multiverse of Madness",
                    video = false,
                    voteAverage = 7.5,
                    voteCount = 4122
                )
            )
        )
    }


}