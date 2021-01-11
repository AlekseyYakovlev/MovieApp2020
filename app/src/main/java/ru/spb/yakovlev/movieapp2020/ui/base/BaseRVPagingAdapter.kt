package ru.spb.yakovlev.movieapp2020.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


class BaseRVPagingAdapter<T : ViewBinding, R : RvItemData>(
    private val viewHolderInflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    private val viewHolderBinder: (T, R) -> Unit
) : PagingDataAdapter<R, BaseRVPagingAdapter.BaseVH<T, R>>(BaseDiffCallback<R>()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH<T, R> {
        val binding = viewHolderInflater(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BaseVH(binding, viewHolderBinder)
    }

    override fun onBindViewHolder(holder: BaseVH<T, R>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class BaseDiffCallback<R : RvItemData> : DiffUtil.ItemCallback<R>() {
        override fun areItemsTheSame(oldItem: R, newItem: R): Boolean =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: R, newItem: R): Boolean =
            oldItem.equals(newItem)
    }

    class BaseVH<T : ViewBinding, R : RvItemData>(
        private val binding: T,
        private val bindVH: (T, R) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(itemData: R) {
            bindVH(binding, itemData)
        }
    }
}