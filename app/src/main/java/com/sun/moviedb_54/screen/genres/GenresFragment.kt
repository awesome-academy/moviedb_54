package com.sun.moviedb_54.screen.genres

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sun.moviedb_54.R
import com.sun.moviedb_54.data.model.GenresResult
import com.sun.moviedb_54.databinding.FragmentGenresBinding
import com.sun.moviedb_54.extensions.addFragment
import com.sun.moviedb_54.screen.detailmovie.DetailMovieFragment
import com.sun.moviedb_54.screen.genres.adapter.GenresAdapter
import com.sun.moviedb_54.screen.genres.adapter.GenresMovieAdapter
import com.sun.moviedb_54.screen.genres.adapter.GenresSelectedAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class GenresFragment : Fragment() {

    private lateinit var binding: FragmentGenresBinding
    private lateinit var genresAdapter: GenresAdapter
    private lateinit var genresMovieAdapter: GenresMovieAdapter
    private lateinit var genresSelectedAdapter: GenresSelectedAdapter
    private val viewModel: GenresViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_genres, container, false)
        binding.apply {
            viewModel = this@GenresFragment.viewModel
            lifecycleOwner = this@GenresFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        registerLiveData()
    }

    private fun initViews() {
        genresAdapter = GenresAdapter { genresResult: GenresResult, position: Int ->
            binding.recyclerViewGenresMovie.layoutManager?.scrollToPosition(0)
            genresAdapter.selectedGenres(position)
            genresResult.positionSelected = position
            with(viewModel) {
                addGenresSelected(genresResult)
                getGenresMovie(getGenresSelected())
            }
        }
        genresMovieAdapter = GenresMovieAdapter {
            addFragment(DetailMovieFragment.newInstance(it), R.id.mainFrame)
        }
        genresSelectedAdapter =
            GenresSelectedAdapter { positionSelected: Int?, genresResult: GenresResult ->
                binding.recyclerViewGenresMovie.layoutManager?.scrollToPosition(0)
                genresAdapter.unselectedGenres(positionSelected)
                with(viewModel) {
                    removeGenresSelected(genresResult)
                    getGenresMovie(getGenresSelected())
                }
            }
        with(binding) {
            genresAdapter = this@GenresFragment.genresAdapter
            genresMovieAdapter = this@GenresFragment.genresMovieAdapter
            genresSelectedAdapter = this@GenresFragment.genresSelectedAdapter
        }
    }

    private fun registerLiveData() {
        genresResultObserve()
    }

    private fun genresResultObserve() {
        viewModel.genresResults.observe(viewLifecycleOwner, Observer { genresResults ->
            genresResults.first().let {
                it.isSelected = true
                it.positionSelected = 0
                with(viewModel) {
                    addGenresSelected(it)
                    getGenresMovie(getGenresSelected())
                }
            }
        })
    }

    companion object {
        fun newInstance() = GenresFragment()
    }
}
