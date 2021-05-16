package com.sun.moviedb_54.screen.genres.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.moviedb_54.R
import com.sun.moviedb_54.data.model.GenresMovieResult
import com.sun.moviedb_54.databinding.ItemGenresMovieBinding
import com.sun.moviedb_54.databinding.ItemLoadMoreBinding
import com.sun.moviedb_54.screen.genres.adapter.itemviewholder.GenresMovieItemViewModel
import com.sun.moviedb_54.ultis.BindingDataRecyclerView
import kotlinx.android.synthetic.main.item_genres_movie.view.*

class GenresMovieAdapter(private val onClickListener: (Int) -> Unit) :
    ListAdapter<GenresMovieResult, RecyclerView.ViewHolder>(GenresMovieDiffUtil()),
    BindingDataRecyclerView<MutableList<GenresMovieResult>> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOADING) {
            val binding = DataBindingUtil.inflate<ItemLoadMoreBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_load_more,
                parent,
                false
            )
            LoadItemViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemGenresMovieBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_genres_movie,
                parent,
                false
            )
            GenresMovieViewHolder(binding, onClickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GenresMovieViewHolder) {
            holder.bindData(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun setData(data: MutableList<GenresMovieResult>?) {
        data?.let {
            submitList(it.toMutableList())
        }
    }

    class GenresMovieViewHolder(
        private val binding: ItemGenresMovieBinding,
        private val onClickListener: (Int) -> Unit,
        private val itemViewModel: GenresMovieItemViewModel = GenresMovieItemViewModel(
            onClickListener
        )
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = itemViewModel
        }

        fun bindData(genresMovieResult: GenresMovieResult) {
            itemViewModel.setData(genresMovieResult)
            binding.root.imageViewGenresMovie.setOnClickListener {
                genresMovieResult.id?.let { it1 -> onClickListener(it1) }
            }
            binding.executePendingBindings()
        }
    }

    class LoadItemViewHolder(binding: ItemLoadMoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    class GenresMovieDiffUtil : DiffUtil.ItemCallback<GenresMovieResult>() {

        override fun areItemsTheSame(
            oldItem: GenresMovieResult,
            newItem: GenresMovieResult
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GenresMovieResult,
            newItem: GenresMovieResult
        ): Boolean {
            return oldItem == newItem
        }
    }

    companion object {

        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
    }
}
