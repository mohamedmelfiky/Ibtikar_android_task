package com.elfiky.ibtikarandroidtask.persondetails

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.elfiky.domain.entities.ProfileImage
import com.elfiky.ibtikarandroidtask.R
import com.elfiky.ibtikarandroidtask.databinding.ProfileImageItemBinding

class ProfileImageViewHolder(
    private val binding: ProfileImageItemBinding
) : RecyclerView.ViewHolder(
    binding.root
) {

    fun bind(item: ProfileImage, onClick: (item: ProfileImage) -> Unit) {
        with(binding) {
            personImg.load(item.thumbnail.url) {
                crossfade(true)
                transformations(RoundedCornersTransformation(radius = 16f))
                placeholder(R.drawable.image_place_holder)
            }
            root.setOnClickListener { onClick(item) }
        }
    }
}