package com.elfiky.ibtikarandroidtask.popularlist

import androidx.recyclerview.widget.DiffUtil
import com.elfiky.domain.entities.PersonOverview

object PersonOverviewDiff : DiffUtil.ItemCallback<PersonOverview>() {
    override fun areItemsTheSame(oldItem: PersonOverview, newItem: PersonOverview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PersonOverview, newItem: PersonOverview): Boolean {
        return oldItem == newItem
    }
}