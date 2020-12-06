package ru.spb.yakovlev.androidacademy2020.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.spb.yakovlev.androidacademy2020.R
import ru.spb.yakovlev.androidacademy2020.ui.movie_details.MovieDetailsFragment
import ru.spb.yakovlev.androidacademy2020.ui.movies_list.MoviesListFragment

class RootActivity : AppCompatActivity() {
    private val navigateToMovieDetails: (Int) -> Unit = { id ->
        val args = Bundle().apply {
            putInt(MOVIE_ID, id)
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
                    MOVIES_LIST_FRAGMENT
                )
            }
        } else (supportFragmentManager.findFragmentByTag(MOVIES_LIST_FRAGMENT) as? MoviesListFragment)?.let {
            it.clickListener = navigateToMovieDetails
        }
    }

    companion object {
        const val MOVIES_LIST_FRAGMENT = "MOVIES_LIST_FRAGMENT"
        const val MOVIE_ID = "MOVIE_ID"
    }
}