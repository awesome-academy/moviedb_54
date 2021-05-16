package com.sun.moviedb_54.ultis

import androidx.recyclerview.widget.DiffUtil
import com.sun.moviedb_54.data.model.MovieFavorite

class FavoriteDiffCallBack : DiffUtil.ItemCallback<MovieFavorite>() {
    override fun areItemsTheSame(oldItem: MovieFavorite, newItem: MovieFavorite): Boolean {
        return oldItem.idMovie == newItem.idMovie
    }

    override fun areContentsTheSame(oldItem: MovieFavorite, newItem: MovieFavorite): Boolean {
        return oldItem == newItem
    }
}
