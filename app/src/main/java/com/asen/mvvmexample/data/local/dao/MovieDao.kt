package com.asen.mvvmexample.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.asen.mvvmexample.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie_entity")
    fun loadMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg movies: MovieEntity)

    @Query("SELECT * FROM movie_entity WHERE id=:movieId")
    fun getMovieById(movieId: Int): Flow<MovieEntity>
}