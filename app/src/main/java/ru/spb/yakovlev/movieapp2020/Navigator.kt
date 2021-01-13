package ru.spb.yakovlev.movieapp2020

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.spb.yakovlev.movieapp2020.ui.RootActivity
import ru.spb.yakovlev.movieapp2020.ui.movie_details.MovieDetailsFragment
import ru.spb.yakovlev.movieapp2020.ui.movies_list.MoviesListFragment
import timber.log.Timber
import javax.inject.Inject

class Navigator @Inject constructor(
    activity: RootActivity
) {
    private val navigationContainerId = R.id.root_container
    private val fragmentManager = activity.supportFragmentManager

    fun navigateTo(destination: Destination, arguments: Int? = null) {

        val args = arguments?.let { argument ->
            Bundle().apply {
                destination.argTag?.let { putInt(it, argument) }
                    ?: run { Timber.e("No ARGUMENT_TAG in destination fragment ${destination.fragmentTag}") }
            }
        }

        fragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.zoom_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.zoom_out,
            )
            replace(
                navigationContainerId,
                destination.fragmentClass,
                args,
                destination.fragmentTag
            )
            addToBackStack(null)
        }
    }

    enum class Destination(
        val fragmentClass: Class<out Fragment?>,
        val fragmentTag: String? = null,
        val argTag: String? = null,
    ) {
        MOVIE_DETAILS_FRAGMENT(
            MovieDetailsFragment::class.java,
            "MOVIE_DETAILS_FRAGMENT",
            MovieDetailsFragment.ARGUMENT_TAG
        ),
        MOVIES_LIST_FRAGMENT(
            MoviesListFragment::class.java,
            "MOVIES_LIST_FRAGMENT"
        ),
    }
}