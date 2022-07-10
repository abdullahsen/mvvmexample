package com.asen.mvvmexample.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.asen.mvvmexample.data.local.dao.MovieDao
import com.asen.mvvmexample.data.local.entity.MovieEntity
import com.asen.mvvmexample.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class MovieEntityReadyAndWriteTest {

    private lateinit var movieDao: MovieDao
    private lateinit var appDatabase: AppDatabase

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()
        movieDao = appDatabase.movieDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun writeMovieAndGetMovieById() = runBlocking  {
        val movieEntity = MovieEntity(
            id = 453395,
            title = "Doctor Strange in the Multiverse of Madness",
            description = "Doctor Strange, with the help of mystical allies both old and new, traverses the mind-bending and dangerous alternate realities of the Multiverse to confront a mysterious new adversary.",
            imageUrl = "/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg",
            releaseDate = "2022-05-04",
            voteAverage = 7.5
        )
        movieDao.insertMovies(*listOf(movieEntity).toTypedArray())
        val byId = movieDao.getMovieById(453395).asLiveData().getOrAwaitValue()
        assertThat(byId, equalTo(movieEntity))
    }

    @Test
    fun writeMoviesAndGetMovieList() = runBlocking  {
        val movieEntity1 = MovieEntity(
            id = 453395,
            title = "Doctor Strange in the Multiverse of Madness",
            description = "Doctor Strange, with the help of mystical allies both old and new, traverses the mind-bending and dangerous alternate realities of the Multiverse to confront a mysterious new adversary.",
            imageUrl = "/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg",
            releaseDate = "2022-05-04",
            voteAverage = 7.5
        )
        val movieEntity2 = MovieEntity(
            id = 453396,
            title = "Doctor Strange in the Multiverse of Madness",
            description = "Doctor Strange, with the help of mystical allies both old and new, traverses the mind-bending and dangerous alternate realities of the Multiverse to confront a mysterious new adversary.",
            imageUrl = "/9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg",
            releaseDate = "2022-05-04",
            voteAverage = 7.5
        )
        movieDao.insertMovies(*listOf(movieEntity1, movieEntity2).toTypedArray())
        val movieList = movieDao.loadMovies().asLiveData().getOrAwaitValue()
        assertThat(movieList, equalTo(listOf(movieEntity1, movieEntity2)))
        assertThat(movieList.size, equalTo(2))
    }


}