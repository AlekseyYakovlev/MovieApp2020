package ru.spb.yakovlev.movieapp2020.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.metadata
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.FragmentActorItemBinding
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMovieDetailsBinding
import ru.spb.yakovlev.movieapp2020.model.ActorItemData
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.MovieDetailsDataWithCast
import ru.spb.yakovlev.movieapp2020.ui.base.BaseRVAdapter
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemPadding
import ru.spb.yakovlev.movieapp2020.utils.viewbindingdelegate.viewBinding

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val viewModel: MovieDetailsViewModel by viewModels()
    private val vb by viewBinding(FragmentMovieDetailsBinding::bind)
    private val actorsRvAdapter by lazy(::setupRecyclerViewAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movieId = arguments?.getInt(ARGUMENT_TAG, 0) ?: 0
        setupViews(movieId)
    }

    private fun setupViews(movieId: Int) {
        vb.movieDetailsRoot.addSystemPadding()

        vb.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        vb.rvActorsList.apply {
            adapter = actorsRvAdapter
            setHasFixedSize(true)
        }

        val language = resources.getString(R.string.app_locale)
        val country = resources.getString(R.string.app_default_location)

        lifecycleScope.launchWhenStarted {
            viewModel.showMovieDetails(movieId, language).collectLatest {
                renderState(it, country)
            }
        }
    }

    private fun renderState(state: DataState<MovieDetailsDataWithCast>, country: String) {
        when (state) {
            is DataState.Empty -> {
            }
            is DataState.Loading -> {
            }
            is DataState.Success<MovieDetailsDataWithCast> -> {
                with(state.data) {
                    vb.ivPoster.load(backdrop) {
                        placeholderMemoryCacheKey(vb.ivPoster.metadata?.memoryCacheKey)
                    }

                    lifecycleScope.launchWhenResumed {

                        val certification = viewModel.getCertification(id, country)
                        if (certification.isNotBlank()) {
                            vb.tvPg.isVisible = true
                            vb.tvPg.text = certification
                        } else vb.tvPg.isInvisible = true

                    }
                    if (minimumAge.isNotBlank()) {
                        vb.tvPg.isVisible = true
                        vb.tvPg.text = minimumAge
                    } else vb.tvPg.isInvisible = true

                    vb.ivLike.apply {
                        isChecked = isLike
                        setOnClickListener { viewModel.handleLike(state.data.id, !isLike) }
                    }
                    vb.tvTitle.text = title
                    vb.tvTags.text = genre
                    vb.ratingBar.rating = voteAverage
                    vb.tvReview.text = resources.getQuantityString(
                        R.plurals.movie_details__reviews,
                        numberOfRatings,
                        numberOfRatings
                    )
                    vb.tvStorylineText.text = overview

                    vb.tvMovieCast.isVisible = actorItemsData.isNotEmpty()
                    vb.rvActorsList.isVisible = actorItemsData.isNotEmpty()

                    if (actorItemsData.isNotEmpty()) {
                        actorsRvAdapter.updateData(actorItemsData)
                    }
                }
            }
            is DataState.Error -> {
                Snackbar.make(vb.root, state.errorMessage, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecyclerViewAdapter() =
        BaseRVAdapter<FragmentActorItemBinding, ActorItemData>(
            viewHolderInflater = { layoutInflater, parent, attachToParent ->
                FragmentActorItemBinding.inflate(layoutInflater, parent, attachToParent)
            },
            viewHolderBinder = { holder, itemData ->
                with(holder) {
                    if (itemData.photo.isNotBlank()) {
                        ivPhoto.load(itemData.photo) {
                            placeholder(R.drawable.ic_avatar_placeholder)
                        }
                    } else {
                        ivPhoto.load(R.drawable.ic_avatar_placeholder)
                    }
                    tvName.text = itemData.name
                }
            },
        )

    companion object {
        const val ARGUMENT_TAG = "MOVIE_ID"
    }
}