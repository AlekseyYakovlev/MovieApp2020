package ru.spb.yakovlev.androidacademy2020.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMoviesListBinding

class MoviesListFragment : Fragment() {
    var clickListener: (() -> Unit)? = null

    private var _binding: FragmentMoviesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickListener = clickListener2

        binding.card1.root.setOnClickListener {
            clickListener?.invoke()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var clickListener2: (() -> Unit)? = null
    }
}

