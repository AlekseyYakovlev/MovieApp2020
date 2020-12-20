package ru.spb.yakovlev.movieapp2020.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.metadata
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.FragmentActorItemBinding
import ru.spb.yakovlev.movieapp2020.databinding.FragmentMovieDetailsBinding
import ru.spb.yakovlev.movieapp2020.model.ActorItemData
import ru.spb.yakovlev.movieapp2020.model.DataState
import ru.spb.yakovlev.movieapp2020.model.MovieDetailsData
import ru.spb.yakovlev.movieapp2020.ui.RootActivity
import ru.spb.yakovlev.movieapp2020.ui.base.BaseRVAdapter
import ru.spb.yakovlev.movieapp2020.ui.util.addSystemPadding
import ru.spb.yakovlev.movieapp2020.utils.viewbindingdelegate.viewBinding
import ru.spb.yakovlev.movieapp2020.viewmodels.MovieDetailsViewModel

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val viewModel: MovieDetailsViewModel by viewModels()
    private val vb by viewBinding(FragmentMovieDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        val movieId = arguments?.getInt(RootActivity.ARG_TAG__MOVIE_ID, 0) ?: 0
        viewModel.setMovieId(movieId)

        vb.movieDetailsRoot.addSystemPadding()

        vb.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val rvAdapter = setupRecyclerViewAdapter()
        vb.rvActorsList.apply {
            adapter = rvAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.movieDetailsState.collect {
                when (it) {
                    is DataState.Empty -> {
                        vb.tvMovieCast
                    }
                    is DataState.Loading -> {
                    }
                    is DataState.Success<MovieDetailsData> -> {
                        with(it.data) {
                            vb.ivPoster.load(backdrop) {
                                placeholderMemoryCacheKey(vb.ivPoster.metadata?.memoryCacheKey)
                            }
                            vb.tvPg.text = minimumAge
                            vb.ivLike.apply {
                                isChecked = isLike
                                setOnClickListener { viewModel.handleLike(movieId, !isLike) }
                            }
                            vb.tvTitle.text = title
                            vb.tvTags.text = genre
                            vb.ratingBar.rating = ratings
                            vb.tvReview.text = resources.getQuantityString(
                                R.plurals.movie_details__reviews,
                                numberOfRatings,
                                numberOfRatings
                            )
                            vb.tvStorylineText.text = overview

                            vb.tvMovieCast.isVisible = actorItemsData.isNotEmpty()
                            vb.rvActorsList.isVisible = actorItemsData.isNotEmpty()

                            if (actorItemsData.isNotEmpty()) {
                                rvAdapter.updateData(actorItemsData)
                            }
                        }
                    }
                    is DataState.Error -> {
                        Snackbar.make(vb.root, it.errorMessage, Snackbar.LENGTH_LONG).show()
                    }
                }
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
                    ivPhoto.load(itemData.photo) {
                        placeholderMemoryCacheKey(ivPhoto.metadata?.memoryCacheKey)
                    }
                    tvName.text = itemData.name
                }
            },
        )
}