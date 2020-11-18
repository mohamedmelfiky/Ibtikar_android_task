package com.elfiky.ibtikarandroidtask.popularlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.elfiky.domain.entities.PersonOverview
import com.elfiky.ibtikarandroidtask.databinding.PersonOverviewItemBinding

class PopularAdapter(
    diffCallback: DiffUtil.ItemCallback<PersonOverview> = PersonOverviewDiff,
    private val onClick: (item: PersonOverview) -> Unit = {}
) : PagingDataAdapter<PersonOverview, PersonOverviewViewHolder>(
    diffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonOverviewViewHolder {
        val binding = PersonOverviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PersonOverviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PersonOverviewViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) 1 else 0
    }

}