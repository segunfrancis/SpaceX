package com.segunfrancis.spacex.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.segunfrancis.spacex.R
import com.segunfrancis.spacex.databinding.ItemHeaderBinding

class HeaderAdapter(@StringRes private val title: Int) :
    RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(
            ItemHeaderBinding.bind(
                LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(title)
    }

    override fun getItemCount(): Int = 1

    inner class HeaderViewHolder(private val binding: ItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(@StringRes title: Int) = with(binding) {
            labelCompany.text = root.context.getString(title)
        }
    }
}
