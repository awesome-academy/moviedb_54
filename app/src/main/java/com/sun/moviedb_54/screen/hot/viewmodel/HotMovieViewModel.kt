package com.sun.moviedb_54.screen.hot.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.moviedb_54.data.model.MovieResult
import com.sun.moviedb_54.data.source.repository.MovieRepository
import com.sun.moviedb_54.extensions.plusAssign
import com.sun.moviedb_54.ultis.Constant
import com.sun.moviedb_54.ultis.HotMovieType
import com.sun.moviedb_54.ultis.LoadMoreRecyclerViewListener
import kotlinx.coroutines.launch

class HotMovieViewModel(private val movieRepository: MovieRepository) : ViewModel(),
    LoadMoreRecyclerViewListener {

    private var currentPage = Constant.DEFAULT_PAGE

    private val _typeHotMovie = MutableLiveData<HotMovieType>(HotMovieType.POPULAR)
    private val typeHotMovie: MutableLiveData<HotMovieType>
        get() = _typeHotMovie

    private val _isLoad = MutableLiveData<Boolean>()
    val isLoad: LiveData<Boolean>
        get() = _isLoad

    private val _movies = MutableLiveData<MutableList<MovieResult?>>()
    val movieResult: MutableLiveData<MutableList<MovieResult?>>
        get() = _movies

    override fun onLoadData() {
        _isLoad.value = true
        addLoadingMovie()
        Handler(Looper.getMainLooper()).postDelayed({
            removeLoadingMovie()
            currentPage++
            typeHotMovie.value?.let {
                fetchDataHotMovie(currentPage, it)
            }
            _isLoad.value = false
        }, Constant.DELAY_SECOND)
    }

    fun fetchDataHotMovie(
        numberPage: Int = Constant.DEFAULT_PAGE,
        typeHotMovie: HotMovieType
    ) {
        viewModelScope.launch {
            try {
                movieRepository.getHotMovie(typeHotMovie, numberPage).apply {
                    body()?.let {
                        _movies.plusAssign(it.results)
                    }
                }
            } catch (e: Exception) {
                e.localizedMessage
            }
        }
    }

    private fun addLoadingMovie() {
        _movies.plusAssign(MovieResult())
    }

    fun addHotMovieChange(typeHotMovie: HotMovieType) {
        _typeHotMovie.postValue(typeHotMovie)
        currentPage = Constant.DEFAULT_PAGE
        _movies.value?.clear()
    }

    private fun removeLoadingMovie() {
        if (_isLoad.value == true) {
            _movies.value?.let {
                it.removeAt(it.size - 1)
            }
        }
    }
}
