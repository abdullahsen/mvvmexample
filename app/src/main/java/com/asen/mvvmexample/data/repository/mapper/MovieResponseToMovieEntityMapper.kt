package com.asen.mvvmexample.data.repository.mapper

import com.asen.mvvmexample.data.local.entity.MovieEntity
import com.asen.mvvmexample.data.remote.response.MoviesResponse
import com.asen.mvvmexample.util.Mapper

class MovieResponseToMovieEntityMapper: Mapper<MoviesResponse.Result, MovieEntity> {
    override fun map(input: MoviesResponse.Result): MovieEntity {
        return MovieEntity(
            id = input.id,
            title = input.title,
            description = input.overview,
            imageUrl = input.posterPath,
            releaseDate = input.releaseDate,
            voteAverage = input.voteAverage
        )
    }
}