package com.asen.mvvmexample.ui.view.overview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asen.mvvmexample.databinding.MovieItemBinding
import com.asen.mvvmexample.model.MovieModel
import com.bumptech.glide.RequestManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieListAdapter(private val clickListener: MovieItemClickListener) :
    ListAdapter<MovieModel, MovieListAdapter.MovieViewHolder>(diffCallBack) {

    class MovieViewHolder private constructor(private val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root), KoinComponent {

        private val glide: RequestManager by inject()

        fun bind(item: MovieModel, clickListener: MovieItemClickListener) {
            val imageUrl = "https://image.tmdb.org/t/p/w500" + item.image
            glide.load(imageUrl).into(binding.movieImage)
            binding.movieTitleText.text = item.title
            binding.movieDescriptionText.text = item.description
            binding.movieItemContainer.setOnClickListener { clickListener.listener(item.id) }
        }

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
                return MovieViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, clickListener)
    }
}

val diffCallBack = object : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem.id == newItem.id
    }
}