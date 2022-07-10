package com.asen.mvvmexample.ui.viewmodel.overview

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.asen.mvvmexample.MainDispatcherRule
import com.asen.mvvmexample.data.repository.MovieRepository
import com.asen.mvvmexample.model.MovieModel
import kotlinx.coroutines.*
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
class MoviesOverviewViewModelTest {
    private lateinit var moviesOverviewViewModel: MoviesOverviewViewModel
    private val repository = mock<MovieRepository>()
    private val uiStateObserver = mock<Observer<MoviesOverviewViewModelUIState>>()
    private val navigationObserver = mock<Observer<MoviesOverviewViewModelNavigationTarget>>()

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        moviesOverviewViewModel = MoviesOverviewViewModel(repository)
        moviesOverviewViewModel.uiState.observeForever(uiStateObserver)
        moviesOverviewViewModel.navigation.observeForever(navigationObserver)
    }

    @Test
    fun `verify successful movies response is shown in loaded ui state`() = runTest {
        whenever(repository.getMoviesFromService(any())).thenReturn(flow {
            emit(MOVIE_LIST)
        })
        moviesOverviewViewModel.dispatch(MoviesOverviewViewModelEvent.LoadMovies(PAGE_NUMBER))
        advanceUntilIdle()
        verify(uiStateObserver).onChanged(MoviesOverviewViewModelUIState.ShowLoadingState)
        verify(uiStateObserver).onChanged(
            MoviesOverviewViewModelUIState.ShowMoviesLoadedState(
                MOVIE_LIST
            )
        )
    }

    @Test
    fun `verify empty movie response is showm in Empty state`() = runTest {
        whenever(repository.getMoviesFromService(any())).thenReturn(flow {
            emit(emptyList())
        })
        moviesOverviewViewModel.dispatch(MoviesOverviewViewModelEvent.LoadMovies(PAGE_NUMBER))
        advanceUntilIdle()
        verify(uiStateObserver).onChanged(MoviesOverviewViewModelUIState.ShowLoadingState)
        verify(uiStateObserver).onChanged(MoviesOverviewViewModelUIState.ShowEmptyState)
    }

    @Test
    fun `verify detail click event navigates to Detail screen`() = runTest {
        moviesOverviewViewModel.dispatch(MoviesOverviewViewModelEvent.MovieItemClick(MOVIE_ID))
        verify(navigationObserver).onChanged(
            MoviesOverviewViewModelNavigationTarget.MovieDetails(
                MOVIE_ID
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
        val MOVIE_MODEL_2 = MovieModel(
            id = 2,
            title = "Movie2",
            description = "Description2",
            image = "Image2",
            releaseDate = "ReleaseDate2",
            voteAverage = 4.5
        )
        val MOVIE_LIST = listOf(MOVIE_MODEL_1, MOVIE_MODEL_2)
        const val PAGE_NUMBER = 1
        const val MOVIE_ID = 12
    }

}