package ru.spb.yakovlev.androidacademy2020.ui.movies_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentMovieItemBinding
import ru.spb.yakovlev.androidacademy2020.model.MovieItemData

class MoviesListRVAdapter(
    private val bindVH: (FragmentMovieItemBinding, MovieItemData) -> Unit
): RecyclerView.Adapter<MovieVH>(){

    private val itemsList = mutableListOf<MovieItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val binding = FragmentMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieVH(binding.root, binding, bindVH)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        holder.bind(itemsList[position])
    }

    fun updateData(newItems: List<MovieItemData>) {
        val diffCallback =
            MoviesDiffCallback(itemsList, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemsList.clear()
        itemsList.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }


    class MoviesDiffCallback(
        private val oldList: List<MovieItemData>,
        private val newList: List<MovieItemData>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}

class MovieVH(
    itemView: View,
    private val binding: FragmentMovieItemBinding,
    private val bindVH: (FragmentMovieItemBinding, MovieItemData) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    fun bind(itemData: MovieItemData) {
        bindVH(binding, itemData)
    }
}