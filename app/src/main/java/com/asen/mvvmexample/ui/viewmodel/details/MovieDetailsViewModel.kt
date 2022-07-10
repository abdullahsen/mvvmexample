package com.asen.mvvmexample.ui.viewmodel.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asen.mvvmexample.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableLiveData<MovieDetailsViewModelUiState>()
    val uiState: LiveData<MovieDetailsViewModelUiState>
        get() = _uiState

    fun dispatch(event: MovieDetailsViewModelEvent) {
        when (event) {
            is MovieDetailsViewModelEvent.GetMovieDetails -> loadMovieDetails(event.movieId)
        }
    }

    private fun loadMovieDetails(movieId: Int) = viewModelScope.launch {
        repository.getMovieById(movieId).collect { movieModel ->
            _uiState.value = MovieDetailsViewModelUiState.MovieDetailsLoaded(movieModel)
        }
    }

}