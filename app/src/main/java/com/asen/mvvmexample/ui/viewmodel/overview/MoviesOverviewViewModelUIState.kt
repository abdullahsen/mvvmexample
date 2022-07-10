package com.asen.mvvmexample.ui.viewmodel.overview

import com.asen.mvvmexample.model.MovieModel

sealed class MoviesOverviewViewModelUIState {
    data class ShowMoviesLoadedState(val movieList: List<MovieModel>) : MoviesOverviewViewModelUIState()
    object ShowLoadingState : MoviesOverviewViewModelUIState()
    object ShowEmptyState : MoviesOverviewViewModelUIState()
}
