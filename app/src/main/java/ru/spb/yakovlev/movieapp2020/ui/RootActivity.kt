package ru.spb.yakovlev.movieapp2020.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import ru.spb.yakovlev.movieapp2020.Navigator
import ru.spb.yakovlev.movieapp2020.R
import ru.spb.yakovlev.movieapp2020.background.WorkRepository
import ru.spb.yakovlev.movieapp2020.databinding.ActivityRootBinding
import ru.spb.yakovlev.movieapp2020.ui.util.handleLeftAndRightInsets
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
        vb.rootContainer.handleLeftAndRightInsets()

        val workRepository = WorkRepository()
        WorkManager.getInstance(this).enqueue(workRepository.workRequest)
    }

    private fun setupNavigation(isInitial: Boolean) {
        if (isInitial) {
            navigator.navigateTo(Navigator.Destination.MOVIES_LIST_FRAGMENT)
        }
    }
}