package com.asen.mvvmexample.ui.view

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.asen.mvvmexample.R
import org.hamcrest.CoreMatchers
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherScreenTest {

    @Test
    fun testNavigationToDetailScreen() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        val weatherScenario = launchFragmentInContainer<WeatherFragment>()
        weatherScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        onView(ViewMatchers.withId(R.id.city_name_text)).perform(ViewActions.click())
        ViewMatchers.assertThat(
            navController.currentDestination?.id,
            CoreMatchers.`is`(R.id.action_weatherFragment_to_detailFragment))
    }
}