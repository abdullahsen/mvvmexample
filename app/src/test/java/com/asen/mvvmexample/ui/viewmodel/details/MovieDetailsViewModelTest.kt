package com.asen.mvvmexample.ui.viewmodel.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.asen.mvvmexample.MainDispatcherRule
import com.asen.mvvmexample.data.repository.MovieRepository
import com.asen.mvvmexample.model.MovieModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MovieDetailsViewModelTest {
    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var movieDetailsViewModel: MovieDetailsViewModel
    private val repository = mock<MovieRepository>()
    private val uiStateObserver = mock<Observer<MovieDetailsViewModelUiState>>()

    @Before
    fun setUp() {
        movieDetailsViewModel = MovieDetailsViewModel(repository)
        movieDetailsViewModel.uiState.observeForever(uiStateObserver)
    }

    @Test
    fun `verify cached movie is shown in loaded ui state`() = runTest {
        whenever(repository.getMovieById(any())).thenReturn(flow { emit(MOVIE_MODEL_1) })
        movieDetailsViewModel.dispatch(MovieDetailsViewModelEvent.GetMovieDetails(MOVIE_ID))
        advanceUntilIdle()
        verify(uiStateObserver).onChanged(
            MovieDetailsViewModelUiState.MovieDetailsLoaded(
                MOVIE_MODEL_1
            )
        )
    }

    private companion object {
        val MOVIE_MODEL_1 = MovieModel(
            id = 1,
            title = "Movie1",
            description = "Description1",
            image = "Image1",
            releaseDate = "ReleaseDate1",
            voteAverage = 4.8
        )
        const val MOVIE_ID = 12
    }
}