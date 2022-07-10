package com.asen.mvvmexample.data.repository.mapper

import com.asen.mvvmexample.data.local.entity.MovieEntity
import com.asen.mvvmexample.model.MovieModel
import com.asen.mvvmexample.util.Mapper

class MovieEntityToMovieModelMapper : Mapper<MovieEntity, MovieModel> {
    override fun map(input: MovieEntity): MovieModel {
        return MovieModel(
            id = input.id,
            title = input.title,
            description = input.description,
            image = input.imageUrl,
            releaseDate = input.releaseDate,
            voteAverage = input.voteAverage
        )
    }
}