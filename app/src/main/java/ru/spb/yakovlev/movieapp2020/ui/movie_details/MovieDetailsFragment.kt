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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.FragmentActorItemBinding
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMovieDetailsBinding
import ru.spb.yakovlev.movieapp2020.model.ActorItemData
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
            viewModel.showMovieDetails(movieId, language, country).collectLatest (::renderState)
        }
    }

    private fun renderState(state: MovieDetailsScreenState) {
        with(state) {
            vb.ivPoster.load(poster) {
                placeholderMemoryCacheKey(vb.ivPoster.metadata?.memoryCacheKey)
            }

            if (certification.isNotBlank()) {
                vb.tvPg.isInvisible = false
                vb.tvPg.text = certification
            } else vb.tvPg.isInvisible = true

            if (certification.isNotBlank()) {
                vb.tvPg.isInvisible = false
                vb.tvPg.text = certification
            } else vb.tvPg.isInvisible = true

            vb.ivLike.apply {
                isChecked = isLike
                setOnClickListener { viewModel.handleLike(state.id, !isLike) }
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