package com.asen.mvvmexample.ui.view.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.asen.mvvmexample.databinding.FragmentDetailsBinding
import com.asen.mvvmexample.model.MovieModel
import com.asen.mvvmexample.ui.viewmodel.details.MovieDetailsViewModel
import com.asen.mvvmexample.ui.viewmodel.details.MovieDetailsViewModelEvent
import com.asen.mvvmexample.ui.viewmodel.details.MovieDetailsViewModelUiState
import com.bumptech.glide.RequestManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : Fragment() {

    private val detailsViewModel: MovieDetailsViewModel by viewModel()
    private val glide: RequestManager by inject()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        detailsViewModel.dispatch(MovieDetailsViewModelEvent.GetMovieDetails(args.movieId))
        detailsViewModel.uiState.observe(viewLifecycleOwner) { renderUi(it) }
    }

    private fun renderUi(state: MovieDetailsViewModelUiState) {
        when (state) {
            is MovieDetailsViewModelUiState.MovieDetailsLoaded -> populateViews(state.movieModel)
        }
    }

    private fun populateViews(movieModel: MovieModel) {
        with(binding) {
            titleText.text = movieModel.title
            descriptionText.text = movieModel.description
            yearText.text = movieModel.releaseDate
            voteText.text = movieModel.voteAverage.toString()
            val imageUrl = "https://image.tmdb.org/t/p/w500" + movieModel.image
            glide.load(imageUrl).into(movieImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}