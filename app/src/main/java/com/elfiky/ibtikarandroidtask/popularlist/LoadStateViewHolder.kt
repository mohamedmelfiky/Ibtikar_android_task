package com.elfiky.ibtikarandroidtask.popularlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.elfiky.data.extinsions.toFailure
import com.elfiky.domain.entities.InvalidApiKey
import com.elfiky.domain.entities.NetworkFailure
import com.elfiky.ibtikarandroidtask.R
import com.elfiky.ibtikarandroidtask.databinding.LoadStateFooterViewItemBinding

class LoadStateViewHolder(
    private val binding: LoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading

        if (loadState is LoadState.Error) {
            val context = binding.errorMsg.context
            val failure = loadState.error.toFailure()
            binding.errorMsg.text = when (failure) {
                NetworkFailure -> context.getString(R.string.error_network)
                InvalidApiKey -> context.getString(R.string.error_invalid_api_key)
                else -> context.getString(R.string.error_unknown)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.load_state_footer_view_item, parent, false)
            val binding = LoadStateFooterViewItemBinding.bind(view)
            return LoadStateViewHolder(binding, retry)
        }
    }
}