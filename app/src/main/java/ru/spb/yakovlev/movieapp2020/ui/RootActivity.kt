package ru.spb.yakovlev.movieapp2020.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.movieapp2020.Navigator
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.databinding.ActivityRootBinding
import ru.spb.yakovlev.movieapp2020.ui.util.doOnApplyWindowInsets
import ru.spb.yakovlev.movieapp2020.utils.viewbindingdelegate.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class RootActivity : AppCompatActivity(R.layout.activity_root) {
    private val vb by viewBinding(ActivityRootBinding::bind, R.id.root_container)

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation(savedInstanceState == null)
        handleLeftAndRightInsets()
    }

    private fun setupNavigation(isInitial: Boolean) {
        if (isInitial) {
            navigator.navigateTo(Navigator.Destination.MOVIES_LIST_FRAGMENT)
        }
    }

    private fun handleLeftAndRightInsets() {
        vb.rootContainer.doOnApplyWindowInsets { view, insets, initialPadding ->
            val sysInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = initialPadding.left + sysInsets.left,
                right = initialPadding.right + sysInsets.right
            )
            WindowInsetsCompat.Builder(insets).setInsets(
                WindowInsetsCompat.Type.systemBars(),
                Insets.of(
                    0,
                    sysInsets.top,
                    0,
                    sysInsets.bottom
                )
            ).build()
        }
    }
}