package com.elfiky.ibtikarandroidtask.persondetails

import androidx.recyclerview.widget.DiffUtil
import com.elfiky.domain.entities.ProfileImage

object ProfileImageDiff : DiffUtil.ItemCallback<ProfileImage>() {
    override fun areItemsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
        // Id is unique.
        return oldItem.thumbnail == newItem.thumbnail
    }

    override fun areContentsTheSame(oldItem: ProfileImage, newItem: ProfileImage): Boolean {
        return oldItem == newItem
    }
}