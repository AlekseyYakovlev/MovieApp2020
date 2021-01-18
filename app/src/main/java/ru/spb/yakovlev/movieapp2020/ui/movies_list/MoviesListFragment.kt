package ru.spb.yakovlev.movieapp2020.ui.movies_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import coil.load
import coil.metadata
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.movieapp2020.Navigator
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMovieItemBinding
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMoviesListBinding
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import ru.spb.yakovlev.movieapp2020.ui.base.BaseRVPagingAdapter
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemPadding
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemTopPadding
import ru.spb.yakovlev.movieapp2020.utils.viewbindingdelegate.viewBinding
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    private var scrollPosition = 0

    private val viewModel: MoviesListViewModel by viewModels()
    private val vb by viewBinding(FragmentMoviesListBinding::bind)
    private val filmsListRvAdapter by lazy(::setupRecyclerViewAdapter)

    @Inject
    lateinit var navigator: Navigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        Timber.d("123456 setupViews()")
        vb.tvPageTitle.addSystemTopPadding()

        vb.rvMoviesList.apply {
            addSystemPadding()
            filmsListRvAdapter.addLoadStateListener { loadState ->
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            adapter = filmsListRvAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launchWhenResumed {
            Timber.d("123456 lifecycleScope.launch()")
            viewModel.showPopularMovies().collectLatest {
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

                    root.setOnClickListener {
                        navigator.navigateTo(
                            Navigator.Destination.MOVIE_DETAILS_FRAGMENT,
                            itemData.id
                        )
                    }
                }
            },
        )
}


