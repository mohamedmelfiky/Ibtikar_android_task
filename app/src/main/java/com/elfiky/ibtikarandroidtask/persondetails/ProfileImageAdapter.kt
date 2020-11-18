package com.elfiky.ibtikarandroidtask.persondetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.elfiky.domain.entities.ProfileImage
import com.elfiky.ibtikarandroidtask.databinding.ProfileImageItemBinding

class ProfileImageAdapter(
    diffCallback: DiffUtil.ItemCallback<ProfileImage> = ProfileImageDiff,
    private val onClick: (item: ProfileImage) -> Unit = {}
) : ListAdapter<ProfileImage, ProfileImageViewHolder>(diffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileImageViewHolder {
        val binding = ProfileImageItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileImageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }
}