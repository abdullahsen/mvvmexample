package com.asen.mvvmexample.ui.view.overview.adapter

import com.asen.mvvmexample.model.MovieModel

class MovieItemClickListener(val listener: (movieId: Int) -> Unit) {
    fun onMovieClick(movie: MovieModel) = listener(movie.id)
}