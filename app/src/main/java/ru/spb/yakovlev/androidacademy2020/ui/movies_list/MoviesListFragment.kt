package ru.spb.yakovlev.androidacademy2020.ui.movies_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMovieItemBinding
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMoviesListBinding
import ru.spb.yakovlev.androidacademy2020.model.MovieItemData

class MoviesListFragment : Fragment() {
    var clickListener: (() -> Unit)? = null

    val bindVH: (FragmentMovieItemBinding, MovieItemData) -> Unit ={item, data ->
        with(item){

        }

    }

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

        binding.rvMoviesList.apply {
            GridLayoutManager(context, 2)
            adapter = MoviesListRVAdapter(bindVH)
        }

//        binding.card1.root.setOnClickListener {
//            clickListener?.invoke()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

