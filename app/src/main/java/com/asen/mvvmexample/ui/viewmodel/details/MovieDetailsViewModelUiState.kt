package com.asen.mvvmexample.ui.viewmodel.details

import com.asen.mvvmexample.model.MovieModel

sealed class MovieDetailsViewModelUiState{
    data class MovieDetailsLoaded(val movieModel: MovieModel): MovieDetailsViewModelUiState()
}
