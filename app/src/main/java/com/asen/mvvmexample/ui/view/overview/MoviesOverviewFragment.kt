package com.asen.mvvmexample.ui.view.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.asen.mvvmexample.databinding.FragmentOverviewBinding
import com.asen.mvvmexample.model.MovieModel
import com.asen.mvvmexample.ui.view.overview.adapter.MovieItemClickListener
import com.asen.mvvmexample.ui.view.overview.adapter.MovieListAdapter
import com.asen.mvvmexample.ui.viewmodel.overview.MoviesOverviewViewModel
import com.asen.mvvmexample.ui.viewmodel.overview.MoviesOverviewViewModelEvent
import com.asen.mvvmexample.ui.viewmodel.overview.MoviesOverviewViewModelNavigationTarget
import com.asen.mvvmexample.ui.viewmodel.overview.MoviesOverviewViewModelUIState
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesOverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    private val moviesOverviewViewModel: MoviesOverviewViewModel by viewModel()
    private val movieListAdapter = MovieListAdapter(MovieItemClickListener { movieId ->
        handleMovieItemClick(movieId)
    })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initViews()
    }

    private fun initViews() {
        binding.movieList.layoutManager = LinearLayoutManager(requireContext())
        binding.movieList.adapter = movieListAdapter
    }

    private fun initObservers() {
        moviesOverviewViewModel.uiState.observe(viewLifecycleOwner) { renderUIState(it) }
        moviesOverviewViewModel.navigation.observe(viewLifecycleOwner) { handleNavigation(it) }
        moviesOverviewViewModel.pageNumber.observe(viewLifecycleOwner) { fetchNewMovies(it) }
    }

    private fun fetchNewMovies(pageNumber: Int) {
        moviesOverviewViewModel.dispatch(MoviesOverviewViewModelEvent.LoadMovies(pageNumber))
    }

    private fun handleNavigation(target: MoviesOverviewViewModelNavigationTarget) {
        when (target) {
            is MoviesOverviewViewModelNavigationTarget.MovieDetails -> {
                val action =
                    MoviesOverviewFragmentDirections.actionMoviesOverviewFragmentToDetailsFragment(
                        target.movieId
                    )
                binding.root.findNavController().navigate(action)
            }
        }
    }

    fun renderUIState(uiState: MoviesOverviewViewModelUIState) = when (uiState) {
        is MoviesOverviewViewModelUIState.ShowEmptyState -> renderEmptyState()
        is MoviesOverviewViewModelUIState.ShowLoadingState -> renderLoadingState()
        is MoviesOverviewViewModelUIState.ShowMoviesLoadedState -> renderLoadedState(uiState.movieList)
    }

    private fun renderEmptyState() {
        binding.loadingIndicator.visibility = View.INVISIBLE
        binding.movieList.visibility = View.INVISIBLE
        binding.noItemText.visibility = View.VISIBLE
    }

    private fun renderLoadingState() {
        binding.loadingIndicator.visibility = View.VISIBLE
        binding.noItemText.visibility = View.INVISIBLE
    }

    private fun renderLoadedState(movies: List<MovieModel>) {
        movieListAdapter.submitList(movies)
        binding.loadingIndicator.visibility = View.INVISIBLE
        binding.noItemText.visibility = View.INVISIBLE
        binding.movieList.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleMovieItemClick(movieId: Int) {
        moviesOverviewViewModel.dispatch(MoviesOverviewViewModelEvent.MovieItemClick(movieId))
    }

}