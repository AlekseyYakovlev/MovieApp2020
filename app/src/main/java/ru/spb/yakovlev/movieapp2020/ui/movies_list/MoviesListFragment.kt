package ru.spb.yakovlev.movieapp2020.ui.movies_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.metadata
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMovieItemBinding
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMoviesListBinding
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import ru.spb.yakovlev.movieapp2020.ui.base.BaseRVPagingAdapter
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemPadding
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemTopPadding
import ru.spb.yakovlev.movieapp2020.utils.viewbindingdelegate.viewBinding
import timber.log.Timber

@AndroidEntryPoint
class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {
    var clickListener: ((Int) -> Unit)? = null

    var scrollPosition = 0

    private val viewModel: MoviesListViewModel by viewModels()
    private val vb by viewBinding(FragmentMoviesListBinding::bind)
    private val filmsListRvAdapter by lazy(::setupRecyclerViewAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onPause() {
        super.onPause()
        scrollPosition =
            (vb.rvMoviesList.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        Timber.d("123456 onPause $scrollPosition")
    }

    override fun onResume() {
        super.onResume()
        vb.rvMoviesList.scrollToPosition(scrollPosition)
        Timber.d("123456 onResume $scrollPosition")
    }

    private fun setupViews() {
        Timber.d("123456 setupViews()")
        vb.tvPageTitle.addSystemTopPadding()

        vb.rvMoviesList.apply {
            addSystemPadding()
            filmsListRvAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            adapter = filmsListRvAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launch {
            Timber.d("123456 lifecycleScope.launch()")
            viewModel.showPopularMovies().collect {
                filmsListRvAdapter.submitData(it)
            }
        }
    }

    private fun setupRecyclerViewAdapter() =
        BaseRVPagingAdapter<FragmentMovieItemBinding, MovieItemData>(
            viewHolderInflater = { layoutInflater, parent, attachToParent ->
                FragmentMovieItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            viewHolderBinder = { holder, itemData ->
                with(holder) {
                    ivPoster.load(itemData.poster) {
                        placeholderMemoryCacheKey(ivPoster.metadata?.memoryCacheKey)
                    }
                    tvPg.text = itemData.minimumAge
                    ivLike.isChecked = itemData.isLike
                    ivLike.setOnClickListener {
                        viewModel.handleLike(
                            itemData.id,
                            !itemData.isLike
                        )
                    }
                    ratingBar.rating = itemData.voteAverage
                    tvReview.text =
                        resources.getQuantityString(
                            R.plurals.movie_details__reviews,
                            itemData.numberOfRatings,
                            itemData.numberOfRatings
                        )
                    tvHeader.text = itemData.title
                    tvTags.text = itemData.genre

                    if (itemData.runtime > 0) {
                        tvTiming.isVisible = true
                        tvTiming.text = resources.getString(
                            R.string.minutes,
                            itemData.runtime
                        )
                    } else tvTiming.isVisible = true

                    root.setOnClickListener { clickListener?.invoke(itemData.id) }
                }
            },
        )
}


