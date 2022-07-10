package com.asen.mvvmexample.ui.viewmodel.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asen.mvvmexample.data.repository.MovieRepository
import com.asen.mvvmexample.util.SingleLiveEvent
import kotlinx.coroutines.launch

open class MoviesOverviewViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableLiveData<MoviesOverviewViewModelUIState>()
    val uiState: LiveData<MoviesOverviewViewModelUIState>
        get() = _uiState

    private val _navigation = SingleLiveEvent<MoviesOverviewViewModelNavigationTarget>()
    val navigation: LiveData<MoviesOverviewViewModelNavigationTarget>
        get() = _navigation

    private val _pageNumber = MutableLiveData<Int>(1)
    val pageNumber: LiveData<Int>
        get() = _pageNumber

    fun dispatch(event: MoviesOverviewViewModelEvent) {
        when (event) {
            is MoviesOverviewViewModelEvent.MovieItemClick -> navigateToMovieDetail(event.movieId)
            is MoviesOverviewViewModelEvent.LoadMovies -> loadMovies(event.pageNumber)
        }
    }

    private fun navigateToMovieDetail(movieId: Int) {
        _navigation.value = MoviesOverviewViewModelNavigationTarget.MovieDetails(movieId)
    }

    private fun loadMovies(pageNumber: Int) = viewModelScope.launch {
        _uiState.value = MoviesOverviewViewModelUIState.ShowLoadingState
        repository.getMoviesFromService(pageNumber).collect { movieList ->
            if (movieList.isEmpty()) {
                _uiState.value = MoviesOverviewViewModelUIState.ShowEmptyState
            } else {
                _uiState.value = MoviesOverviewViewModelUIState.ShowMoviesLoadedState(movieList)
            }
        }
    }

    fun increasePageNumber() {
        _pageNumber.value?.let { number ->
            _pageNumber.value = number + 1
        }
    }

}