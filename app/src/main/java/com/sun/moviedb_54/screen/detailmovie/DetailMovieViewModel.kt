package com.sun.moviedb_54.screen.detailmovie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.moviedb_54.data.model.Actor
import com.sun.moviedb_54.data.model.DetailMovie
import com.sun.moviedb_54.data.model.MovieResult
import com.sun.moviedb_54.data.source.repository.MovieRepository
import com.sun.moviedb_54.extensions.plusAssign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailMovieViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val _isHideTittle = MutableLiveData<Boolean>(false)
    val isHideTittle: LiveData<Boolean>
        get() = _isHideTittle

    private val _isHideTagLine = MutableLiveData<Boolean>(false)
    val isHideTagLine: LiveData<Boolean>
        get() = _isHideTagLine

    private val _isHideReleaseDate = MutableLiveData<Boolean>(false)
    val isHideReleaseDate: LiveData<Boolean>
        get() = _isHideReleaseDate

    private val _detailMovie = MutableLiveData<DetailMovie?>()
    val detailMovie: LiveData<DetailMovie?>
        get() = _detailMovie

    private val _actors = MutableLiveData<MutableList<Actor>>()
    val actors: LiveData<MutableList<Actor>>
        get() = _actors

    private val _recommendMovie = MutableLiveData<MutableList<MovieResult?>>()
    val recommendMovie: LiveData<MutableList<MovieResult?>>
        get() = _recommendMovie

    fun getDetailMovie(idMovie: Int) {
        try {
            viewModelScope.launch {
                try {
                    launch {
                        movieRepository.getActor(idMovie = idMovie).body()?.also {
                            _actors.plusAssign(it.cast)
                        }
                    }
                    launch {
                        movieRepository.getDetailMovie(idMovie = idMovie).body()?.also {
                            _detailMovie.value = it
                            if (it.title.isNullOrEmpty()) {
                                _isHideTittle.value = true
                            }
                            if (it.tagLine.isNullOrEmpty()) {
                                _isHideTagLine.value = true
                            }
                            if (it.releaseDate.isNullOrEmpty()) {
                                _isHideReleaseDate.value = true
                            }
                        }
                    }
                    launch {
                        movieRepository.getRecommendMovie(idMovie = idMovie).body()?.also {
                            _recommendMovie.plusAssign(it.results)
                        }
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
