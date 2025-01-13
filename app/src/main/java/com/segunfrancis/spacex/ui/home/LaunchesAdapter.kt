package com.segunfrancis.spacex.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.segunfrancis.spacex.ui.home.model.LaunchesUi
import com.segunfrancis.spacex.util.dateDifference
import com.segunfrancis.spacex.util.formatDateAndTime
import com.segunfrancis.spacex.util.formatDateDifference
import com.segunfrancis.spacex.util.loadImage
import com.segunfrancis.spacex.R
import com.segunfrancis.spacex.databinding.ItemLaunchesBinding

class LaunchesAdapter :
    ListAdapter<LaunchesUi, LaunchesAdapter.LaunchesViewHolder>(LaunchDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchesViewHolder {
        return LaunchesViewHolder(
            ItemLaunchesBinding.bind(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_launches, parent, false
                )
            )
        )
    }

    override fun onBindViewHolder(holder: LaunchesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LaunchesViewHolder(private val binding: ItemLaunchesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context: Context by lazy { binding.root.context }
        fun bind(launch: LaunchesUi) = with(binding) {
            missionValue.text = launch.missionName
            rocketValue.text = launch.rocketName.plus(" / ").plus(launch.rocketType)
            val (date, time) = launch.launchDateUtc.formatDateAndTime()
            dateValue.text = context.getString(R.string.text_date_at_time, date, time)
            val dateSinceLaunch = launch.launchDateUnix.dateDifference()
            daysLabel.text = context.getString(
                R.string.text_days_now,
                if (dateSinceLaunch > 0) context.getString(R.string.text_since) else context.getString(
                    R.string.text_from
                )
            )
            daysValue.text = dateSinceLaunch.formatDateDifference()
            if (launch.launchSuccess != null) {
                launchStatusImage.isVisible = true
                launchStatusImage.setImageResource(if (launch.launchSuccess) R.drawable.ic_check else R.drawable.ic_close)
            } else {
                launchStatusImage.isGone = true
            }
            missionPatchImage.loadImage(launch.missionPatchSmall)
            root.setOnClickListener { launch.onClick() }
        }
    }

    companion object {
        val LaunchDiffUtil = object : DiffUtil.ItemCallback<LaunchesUi>() {
            override fun areContentsTheSame(oldItem: LaunchesUi, newItem: LaunchesUi): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: LaunchesUi, newItem: LaunchesUi): Boolean {
                return oldItem.flightNumber == newItem.flightNumber
            }
        }
    }
}
