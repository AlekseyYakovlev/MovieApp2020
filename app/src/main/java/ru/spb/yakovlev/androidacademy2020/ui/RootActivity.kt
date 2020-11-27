package ru.spb.yakovlev.androidacademy2020.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.spb.yakovlev.androidacademy2020.R
import ru.spb.yakovlev.androidacademy2020.ui.movie_details.MovieDetailsFragment
import ru.spb.yakovlev.androidacademy2020.ui.movies_list.MoviesListFragment

class RootActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager

    private val clickListener = {
        fragmentManager.commit {
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
        MoviesListFragment.clickListener2 = clickListener
        val moviesListFragment = MoviesListFragment()

        if (isInitial) {
            fragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.root_container,
                    moviesListFragment
                )
            }
        }
    }
}