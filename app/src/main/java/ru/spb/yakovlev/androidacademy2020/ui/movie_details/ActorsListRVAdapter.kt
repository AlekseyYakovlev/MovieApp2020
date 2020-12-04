package ru.spb.yakovlev.androidacademy2020.ui.movie_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.spb.yakovlev.androidacademy2020.databinding.FragmentActorItemBinding
import ru.spb.yakovlev.androidacademy2020.model.ActorItemData

class ActorsListRVAdapter(
    private val bindVH: (FragmentActorItemBinding, ActorItemData) -> Unit
): RecyclerView.Adapter<ActorVH>(){

    private val itemsList = mutableListOf<ActorItemData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorVH {
        val binding = FragmentActorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ActorVH(binding.root, binding, bindVH)
    }

    override fun getItemCount(): Int = itemsList.size

    override fun onBindViewHolder(holder: ActorVH, position: Int) {
        holder.bind(itemsList[position])
    }

    fun updateData(newItems: List<ActorItemData>) {
        val diffCallback =
            MoviesDiffCallback(itemsList, newItems)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemsList.clear()
        itemsList.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }


    class MoviesDiffCallback(
        private val oldList: List<ActorItemData>,
        private val newList: List<ActorItemData>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}

class ActorVH(
    itemView: View,
    private val binding: FragmentActorItemBinding,
    private val bindVH: (FragmentActorItemBinding, ActorItemData) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    fun bind(itemData: ActorItemData) {
        bindVH(binding, itemData)
    }
}