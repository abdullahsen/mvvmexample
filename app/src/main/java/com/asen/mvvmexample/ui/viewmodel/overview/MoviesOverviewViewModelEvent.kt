package com.asen.mvvmexample.ui.viewmodel.overview

sealed class MoviesOverviewViewModelEvent {
    data class LoadMovies(val pageNumber: Int) : MoviesOverviewViewModelEvent()
    data class MovieItemClick(val movieId: Int) : MoviesOverviewViewModelEvent()
}
