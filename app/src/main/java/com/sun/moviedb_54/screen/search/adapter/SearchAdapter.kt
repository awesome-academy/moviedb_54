package com.sun.moviedb_54.screen.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sun.moviedb_54.R
import com.sun.moviedb_54.data.model.MovieResult
import com.sun.moviedb_54.databinding.ItemLoadMoreBinding
import com.sun.moviedb_54.databinding.ItemSearchMovieBinding
import com.sun.moviedb_54.screen.genres.adapter.GenresMovieAdapter
import com.sun.moviedb_54.screen.search.adapter.itemviewmodel.SearchItemViewModel
import com.sun.moviedb_54.ultis.BindingDataRecyclerView
import kotlinx.android.synthetic.main.item_search_movie.view.*

class SearchAdapter(
    private val onClickListener: (Int) -> Unit
) : ListAdapter<MovieResult, RecyclerView.ViewHolder>(SearchDiffUtil()),
    BindingDataRecyclerView<MutableList<MovieResult>> {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == GenresMovieAdapter.VIEW_TYPE_LOADING) {
            val binding = DataBindingUtil.inflate<ItemLoadMoreBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_load_more,
                parent,
                false
            )
            GenresMovieAdapter.LoadItemViewHolder(binding)
        } else {
            val binding = DataBindingUtil.inflate<ItemSearchMovieBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_search_movie,
                parent,
                false
            )
            SearchViewHolder(binding, onClickListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchViewHolder) {
            holder.bindData(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).id == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun setData(data: MutableList<MovieResult>?) {
        data?.let {
            if (it == currentList) {
                notifyDataSetChanged()
            } else {
                submitList(it)
            }
        }
    }

    class SearchViewHolder(
        private val binding: ItemSearchMovieBinding,
        private val onClickListener: (Int) -> Unit,
        private val itemViewModel: SearchItemViewModel = SearchItemViewModel(onClickListener)
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.viewModel = itemViewModel
        }

        fun bindData(movieResult: MovieResult) {
            itemViewModel.setData(movieResult)
            binding.root.imageViewSearchMovie.setOnClickListener {
                movieResult.id?.let { it1 -> onClickListener(it1) }
            }
            binding.executePendingBindings()
        }
    }

    class SearchDiffUtil : DiffUtil.ItemCallback<MovieResult>() {

        override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
            return oldItem == newItem
        }
    }

    companion object {

        const val VIEW_TYPE_LOADING = 0
        const val VIEW_TYPE_ITEM = 1
    }
}
