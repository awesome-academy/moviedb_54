package com.sun.moviedb_54.module

import com.sun.moviedb_54.screen.detailmovie.DetailMovieViewModel
import com.sun.moviedb_54.screen.favourite.FavoriteViewModel
import com.sun.moviedb_54.screen.genres.GenresViewModel
import com.sun.moviedb_54.screen.hot.viewmodel.HotMovieViewModel
import com.sun.moviedb_54.screen.search.SearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GenresViewModel(get()) }
    viewModel { DetailMovieViewModel(get()) }
    viewModel { HotMovieViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}
