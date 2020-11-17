package com.elfiky.ibtikarandroidtask.popularlist

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.elfiky.domain.entities.PersonOverview
import com.elfiky.ibtikarandroidtask.databinding.PersonOverviewItemBinding

class PersonOverviewViewHolder(
    private val binding: PersonOverviewItemBinding
) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(item: PersonOverview?, onClick: (item: PersonOverview) -> Unit) {
        if (item != null) {
            with(binding) {
                personImg.load(item.image?.url) { crossfade(true) }
                personNameTv.text = item.name.value
                knownForTv.text = item.knownFor.value
                root.setOnClickListener { onClick(item) }
            }
        }
    }

}