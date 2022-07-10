package com.asen.mvvmexample.ui.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asen.mvvmexample.R
import com.asen.mvvmexample.model.MovieModel
import com.asen.mvvmexample.ui.view.overview.MoviesOverviewFragment
import com.asen.mvvmexample.ui.viewmodel.overview.MoviesOverviewViewModelUIState
import com.asen.mvvmexample.withPositionInRecyclerView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MoviesOverviewFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun verifyLoadingSpinnerIsShownInLoadingUiState() {
        launchFragment().onFragment { fragment ->
            fragment.renderUIState(MoviesOverviewViewModelUIState.ShowLoadingState)
        }
        onView(withId(R.id.loading_indicator)).check(matches(isDisplayed()))
    }

    @Test
    fun verifyNoItemsTextIsShownInEmptyState() {
        launchFragment().onFragment { fragment ->
            fragment.renderUIState(MoviesOverviewViewModelUIState.ShowEmptyState)
        }
        onView(withId(R.id.no_item_text)).check(matches(isDisplayed()))
    }

    @Test
    fun verifyListIsShownInMoviesLoadedState() {
        launchFragment().onFragment { fragment ->
            fragment.renderUIState(
                MoviesOverviewViewModelUIState.ShowMoviesLoadedState(
                    listOf(
                        MOVIE_MODEL_1, MOVIE_MODEL_2
                    )
                )
            )
        }
        onView(withPositionInRecyclerView(0, R.id.movie_list))
            .check(matches(hasDescendant(withText("Title 1"))))
        onView(withPositionInRecyclerView(1, R.id.movie_list))
            .check(matches(hasDescendant(withText("Title 2"))))
    }

    private fun launchFragment() =
        launchFragmentInContainer<MoviesOverviewFragment>()

    private companion object {
        val MOVIE_MODEL_1 = MovieModel(
            id = 1,
            title = "Title 1",
            description = "Description 1",
            image = "image1",
            releaseDate = "2022-05-04",
            voteAverage = 7.5
        )

        val MOVIE_MODEL_2 = MovieModel(
            id = 2,
            title = "Title 2",
            description = "Description 2",
            image = "image2",
            releaseDate = "2022-05-04",
            voteAverage = 7.5
        )
    }
}