package ru.spb.yakovlev.androidacademy2020.ui.movie_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.metadata
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import ru.spb.yakovlev.androidacademy2020.R
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentActorItemBinding
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMovieDetailsBinding
import ru.spb.yakovlev.androidacademy2020.model.ActorItemData
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieDetailsData
import ru.spb.yakovlev.androidacademy2020.ui.RootActivity
import ru.spb.yakovlev.androidacademy2020.utils.viewbindingdelegate.viewBinding
import ru.spb.yakovlev.androidacademy2020.viewmodels.MovieDetailsViewModel

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    private val viewModel: MovieDetailsViewModel by viewModels()
    private val vb by viewBinding(FragmentMovieDetailsBinding::bind)

    private val bindVH: (FragmentActorItemBinding, ActorItemData) -> Unit = { item, data ->
        with(item) {
            ivPhoto.load(data.photo) {
                placeholderMemoryCacheKey(ivPhoto.metadata?.memoryCacheKey)
            }
            tvName.text = data.name
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        val movieId = arguments?.getInt(RootActivity.MOVIE_ID, 0) ?: 0
        viewModel.setMovieId(movieId)

        vb.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        val rvAdapter = ActorsListRVAdapter(bindVH)
        vb.rvActorsList.apply { adapter = rvAdapter }

        lifecycleScope.launchWhenStarted {
            viewModel.movieDetailsState.collect {
                when (it) {
                    is DataState.Empty -> {
                    }
                    is DataState.Loading -> {
                    }
                    is DataState.Success<MovieDetailsData> -> {
                        with(it.data) {
                            vb.ivPoster.load(poster2){
                                placeholderMemoryCacheKey(vb.ivPoster.metadata?.memoryCacheKey)
                            }
                            vb.tvPg.text = pg
                            vb.ivLike.apply {
                                isChecked = isLike
                                setOnClickListener { viewModel.handleLike(movieId, !isLike) }
                            }
                            vb.tvTitle.text = title
                            vb.tvTags.text = tags
                            vb.ratingBar.rating = rating
                            vb.tvReview.text = resources.getQuantityString(
                                R.plurals.movie_details__reviews,
                                reviewsCount,
                                reviewsCount
                            )
                            vb.tvStorylineText.text = storyLine

                            rvAdapter.updateData(actorItemsData)
                        }
                    }
                    is DataState.Error -> {
                        Snackbar.make(vb.root, it.errorMessage, Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}