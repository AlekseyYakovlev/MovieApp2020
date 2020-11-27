package ru.spb.yakovlev.androidacademy2020.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.spb.yakovlev.androidacademy2020.R
import ru.spb.yakovlev.androidacademy2020.ui.movie_details.MovieDetailsFragment
import ru.spb.yakovlev.androidacademy2020.ui.movies_list.MoviesListFragment

class RootActivity : AppCompatActivity() {
    private val clickListener = {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.root_container, MovieDetailsFragment())
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
            val moviesListFragment = MoviesListFragment().also { it.clickListener = clickListener }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.root_container,
                    moviesListFragment,
                    MOVIES_LIST_FRAGMENT
                )
            }
        } else (supportFragmentManager.findFragmentByTag(MOVIES_LIST_FRAGMENT) as? MoviesListFragment)?.let {
            it.clickListener = clickListener
        }
    }

    companion object {
        const val MOVIES_LIST_FRAGMENT = "MOVIES_LIST_FRAGMENT"
    }
}