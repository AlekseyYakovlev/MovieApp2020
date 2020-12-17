package ru.spb.yakovlev.androidacademy2020.ui.movies_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.metadata
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.androidacademy2020.R
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMovieItemBinding
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMoviesListBinding
import ru.spb.yakovlev.androidacademy2020.extensions.roundRating
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieItemData
import ru.spb.yakovlev.androidacademy2020.ui.base.BaseRVAdapter
import ru.spb.yakovlev.androidacademy2020.ui.util.addSystemPadding
import ru.spb.yakovlev.androidacademy2020.ui.util.addSystemTopPadding
import ru.spb.yakovlev.androidacademy2020.utils.viewbindingdelegate.viewBinding
import ru.spb.yakovlev.androidacademy2020.viewmodels.MoviesListViewModel

class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {
    var clickListener: ((Int) -> Unit)? = null

    private val viewModel: MoviesListViewModel by viewModels()
    private val vb by viewBinding(FragmentMoviesListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val rvAdapter = setupRecyclerViewAdapter()

        vb.tvPageTitle.addSystemTopPadding()

        vb.rvMoviesList.apply {
            addSystemPadding()
            adapter = rvAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.moviesListState.collectLatest {
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

    private fun setupRecyclerViewAdapter() =
        BaseRVAdapter<FragmentMovieItemBinding, MovieItemData>(
            viewHolderInflater = { layoutInflater, parent, attachToParent ->
                FragmentMovieItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            viewHolderBinder = { item, data ->
                with(item) {
                    ivPoster.load(data.poster) {
                        placeholderMemoryCacheKey(ivPoster.metadata?.memoryCacheKey)
                    }
                    tvPg.text = data.minimumAge
                    ivLike.isChecked = data.isLike
                    ivLike.setOnClickListener { viewModel.handleLike(data.id, !data.isLike) }
                    ratingBar.rating = data.ratings.roundRating()
                    tvReview.text =
                        resources.getQuantityString(
                            R.plurals.movie_details__reviews,
                            data.numberOfRatings,
                            data.numberOfRatings
                        )
                    tvHeader.text = data.title
                    tvTags.text = data.genre
                    tvTiming.text = resources.getString(
                        R.string.minutes,
                        data.runtime
                    )
                    root.setOnClickListener { clickListener?.invoke(data.id) }
                }
            },
        )
}


