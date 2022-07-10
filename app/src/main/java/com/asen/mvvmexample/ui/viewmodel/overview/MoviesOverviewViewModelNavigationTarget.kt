package com.asen.mvvmexample.ui.viewmodel.overview

sealed class MoviesOverviewViewModelNavigationTarget {
    data class MovieDetails(val movieId: Int) : MoviesOverviewViewModelNavigationTarget()
}
