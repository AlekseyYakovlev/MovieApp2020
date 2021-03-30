package ru.spb.yakovlev.movieapp2020.ui.movies_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import coil.ImageLoader
//import coil.load
import coil.metadata
import coil.request.CachePolicy
import coil.request.ImageRequest
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.spb.yakovlev.movieapp2020.Navigator
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMovieItemBinding
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMoviesListBinding
import ru.spb.yakovlev.movieapp2020.model.MovieItemData
import ru.spb.yakovlev.movieapp2020.ui.base.BaseRVAdapter
import ru.spb.yakovlev.movieapp2020.ui.base.BaseRVPagingAdapter
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemPadding
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemTopPadding
import ru.spb.yakovlev.movieapp2020.utils.viewbindingdelegate.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class MoviesListFragment : Fragment(R.layout.fragment_movies_list) {

    private val viewModel: MoviesListViewModel by viewModels()
    private val vb by viewBinding(FragmentMoviesListBinding::bind)
    private val filmsListRvAdapter by lazy(::setupRecyclerViewAdapter)

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        vb.tvPageTitle.addSystemTopPadding()

        vb.rvMoviesList.apply {
            addSystemPadding()

            filmsListRvAdapter.addLoadStateListener { loadState ->
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Whooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            adapter = filmsListRvAdapter
            setHasFixedSize(true)

        }

        val language = resources.getString(R.string.app_locale)
        val country = resources.getString(R.string.app_default_location)

        lifecycleScope.launchWhenResumed {
            viewModel.showPopularMovies(language, country).collectLatest {
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

                    val request = ImageRequest.Builder(requireContext())
                        .data(itemData.poster)
                        .target(ivPoster)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .placeholderMemoryCacheKey(ivPoster.metadata?.memoryCacheKey)
                        .build()
                    imageLoader.enqueue(request)


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

                    lifecycleScope.launchWhenResumed {
                        launch {
                            val certification = viewModel.getCertification(itemData.id)
                            if (certification.isNotBlank()) {
                                tvPg.isVisible = true
                                tvPg.text = certification
                            } else tvPg.isInvisible = true
                        }
                        launch {
                            val runtime = viewModel.getRuntime(itemData.id)
                            if (runtime > 0) {
                                tvTiming.isVisible = true
                                tvTiming.text = resources.getString(
                                    R.string.minutes,
                                    runtime
                                )
                            } else tvTiming.isInvisible = true
                        }
                    }

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


