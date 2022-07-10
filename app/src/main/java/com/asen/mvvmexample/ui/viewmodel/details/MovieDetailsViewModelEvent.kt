package com.asen.mvvmexample.ui.viewmodel.details

sealed class MovieDetailsViewModelEvent {
    data class GetMovieDetails(val movieId: Int) : MovieDetailsViewModelEvent()
}
