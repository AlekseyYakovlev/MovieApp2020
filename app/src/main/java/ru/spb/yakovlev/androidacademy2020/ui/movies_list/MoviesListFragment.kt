package ru.spb.yakovlev.androidacademy2020.ui.movies_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import coil.metadata
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import ru.spb.yakovlev.androidacademy2020.R
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMovieItemBinding
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMoviesListBinding
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieItemData
import ru.spb.yakovlev.androidacademy2020.utils.viewbindingdelegate.viewBinding
import ru.spb.yakovlev.androidacademy2020.viewmodels.MoviesListViewModel

class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {
    var clickListener: ((Int) -> Unit)? = null


    private val viewModel: MoviesListViewModel by viewModels()
    private val vb by viewBinding(FragmentMoviesListBinding::bind)

    private val bindVH: (FragmentMovieItemBinding, MovieItemData) -> Unit = { item, data ->
        with(item) {
            ivPoster.load(data.poster) {
                placeholderMemoryCacheKey(ivPoster.metadata?.memoryCacheKey)
            }
            tvPg.text = data.pg
            ivLike.isChecked = data.isLike
            ivLike.setOnClickListener { viewModel.handleLike(data.id, !data.isLike) }
            ratingBar.rating = data.rating
            tvReview.text =
                resources.getQuantityString(
                    R.plurals.movie_details__reviews,
                    data.reviewsCount,
                    data.reviewsCount
                )
            tvHeader.text = data.title
            tvTags.text = data.tags
            tvTiming.text = resources.getString(R.string.minutes, data.duration)
            root.setOnClickListener { clickListener?.invoke(data.id) }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        viewModel.update()
    }

    private fun setupViews() {
        val rvAdapter = MoviesListRVAdapter(bindVH)
        vb.rvMoviesList.apply {
            layoutManager = GridLayoutManager(
                context,
                resources.getInteger(R.integer.movies_list__number_of_columns)
            )
            adapter = rvAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.moviesListState.collect {
                when (it) {
                    is DataState.Empty -> {
                    }
                    is DataState.Loading -> {
                        vb.progressBar.visibility = View.VISIBLE
                        it.progress?.let { progress ->
                            vb.tvProgressBarText.visibility = View.VISIBLE
                            vb.tvProgressBarText.text =
                                resources.getString(R.string.progress, progress)
                        }
                    }
                    is DataState.Success<List<MovieItemData>> -> {
                        vb.progressBar.visibility = View.GONE
                        vb.tvProgressBarText.visibility = View.GONE
                        rvAdapter.updateData(it.data)
                    }
                    is DataState.Error -> {
                        vb.progressBar.visibility = View.GONE
                        vb.tvProgressBarText.visibility = View.GONE
                        Snackbar.make(vb.root, it.errorMessage, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}

