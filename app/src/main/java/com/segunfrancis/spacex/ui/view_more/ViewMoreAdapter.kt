package com.segunfrancis.spacex.ui.view_more

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.segunfrancis.spacex.R
import com.segunfrancis.spacex.databinding.ItemViewMoreBinding

class ViewMoreAdapter :
    ListAdapter<ViewMoreItem, ViewMoreAdapter.ViewMoreViewHolder>(ViewMoreDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewMoreViewHolder {
        return ViewMoreViewHolder(
            ItemViewMoreBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_view_more, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewMoreViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewMoreViewHolder(private val binding: ItemViewMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ViewMoreItem) = with(binding) {
            viewMoreTitle.text = root.context.getString(item.title)
            viewMoreImage.setImageResource(item.icon)
            root.setOnClickListener { item.onclick.invoke() }
        }
    }

    companion object {
        val ViewMoreDiffUtil = object : DiffUtil.ItemCallback<ViewMoreItem>() {
            override fun areItemsTheSame(oldItem: ViewMoreItem, newItem: ViewMoreItem): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: ViewMoreItem, newItem: ViewMoreItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
