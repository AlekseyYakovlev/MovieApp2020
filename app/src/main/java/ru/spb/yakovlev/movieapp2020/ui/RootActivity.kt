package ru.spb.yakovlev.movieapp2020.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.ActivityRootBinding
import ru.spb.yakovlev.movieapp2020.ui.movie_details.MovieDetailsFragment
import ru.spb.yakovlev.movieapp2020.ui.movies_list.MoviesListFragment
import ru.spb.yakovlev.movieapp2020.ui.util.doOnApplyWindowInsets
import ru.spb.yakovlev.movieapp2020.utils.viewbindingdelegate.viewBinding

class RootActivity : AppCompatActivity(R.layout.activity_root) {
    private val vb by viewBinding(ActivityRootBinding::bind, R.id.root_container)

    private val navigateToMovieDetails: (Int) -> Unit = { id ->
        val args = Bundle().apply {
            putInt(ARG_TAG__MOVIE_ID, id)
        }
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            setCustomAnimations(
                R.anim.zoom_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.zoom_out,
            )
            replace(
                R.id.root_container,
                MovieDetailsFragment::class.java,
                args,
            )
            addToBackStack(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)

        setupNavigation(savedInstanceState == null)
        handleLeftAndRightInsets()
    }

    private fun setupNavigation(isInitial: Boolean) {
        if (isInitial) {
            val moviesListFragment =
                MoviesListFragment().also { it.clickListener = navigateToMovieDetails }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.root_container,
                    moviesListFragment,
                    FRAGMENT_TAG__MOVIES_LIST
                )
            }
        } else (supportFragmentManager.findFragmentByTag(FRAGMENT_TAG__MOVIES_LIST) as? MoviesListFragment)?.let {
            it.clickListener = navigateToMovieDetails
        }
    }

    private fun handleLeftAndRightInsets() {
        vb.rootContainer.doOnApplyWindowInsets { view, insets, initialPadding ->
            view.updatePadding(
                left = initialPadding.left + insets.systemWindowInsetLeft,
                right = initialPadding.right + insets.systemWindowInsetRight
            )
            WindowInsetsCompat.Builder(insets).setSystemWindowInsets(
                Insets.of(
                    0,
                    insets.systemWindowInsetTop,
                    0,
                    insets.systemWindowInsetBottom
                )
            ).build()
        }
    }

    companion object {
        const val FRAGMENT_TAG__MOVIES_LIST = "MOVIES_LIST_FRAGMENT"
        const val ARG_TAG__MOVIE_ID = "MOVIE_ID"
    }
}