package com.elfiky.ibtikarandroidtask.popularlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.elfiky.data.extinsions.toFailure
import com.elfiky.domain.entities.Failures
import com.elfiky.domain.entities.InvalidApiKey
import com.elfiky.domain.entities.NetworkFailure
import com.elfiky.domain.entities.PersonOverview
import com.elfiky.ibtikarandroidtask.R
import com.elfiky.ibtikarandroidtask.databinding.PopularListFragmentBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularListFragment : Fragment() {

    private var _binding: PopularListFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PopularListViewModel>()
    private val adapter = PopularAdapter(onClick = ::onItemClicked)
    private val loadStateLiveData = adapter.loadStateFlow.asLiveData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PopularListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        initRecyclerView()
        loadStateListener()

        lifecycleScope.launch {
            viewModel.popularList.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun loadStateListener() {
        loadStateLiveData.observe(viewLifecycleOwner, { loadState ->
            when (val currentState = loadState.source.refresh) {
                LoadState.Loading -> showLoading()
                is LoadState.NotLoading -> {
                    if (loadState.append.endOfPaginationReached) {
                        if (adapter.itemCount < 1) showEmptyView() else showList()
                    } else {
                        showList()
                    }
                }
                is LoadState.Error -> showError(currentState.error.toFailure())
            }
        })
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (adapter.getItemViewType(position)) {
                    1 -> 2
                    else -> 1
                }
            }
        }

        with(binding) {
            popularListRv.layoutManager = layoutManager
            popularListRv.setHasFixedSize(true)
            popularListRv.adapter = adapter.withLoadStateFooter(LoadStateAdapter(adapter::retry))
        }
    }

    private fun bindViews() {
        binding.retryBtn.setOnClickListener { adapter.retry() }
        // adapter.addLoadStateListener(loadStateListener)
    }

    private fun showLoading() {
        binding.loadingPr.isVisible = true
        binding.popularListRv.isVisible = false
        binding.emptyCl.isVisible = false
        binding.errorCl.isVisible = false
    }

    private fun showEmptyView() {
        binding.loadingPr.isVisible = false
        binding.popularListRv.isVisible = false
        binding.emptyCl.isVisible = true
        binding.errorCl.isVisible = false
    }

    private fun showList() {
        binding.loadingPr.isVisible = false
        binding.popularListRv.isVisible = true
        binding.emptyCl.isVisible = false
        binding.errorCl.isVisible = false
    }

    private fun showError(failure: Failures) {
        binding.loadingPr.isVisible = false
        binding.popularListRv.isVisible = false
        binding.emptyCl.isVisible = false
        binding.errorCl.isVisible = true

        val errorText = when (failure) {
            NetworkFailure -> getString(R.string.error_network)
            InvalidApiKey -> getString(R.string.error_invalid_api_key)
            else -> getString(R.string.error_unknown)
        }
        binding.errorTv.text = errorText
    }

    private fun onItemClicked(item: PersonOverview) {
        findNavController().navigate(
            PopularListFragmentDirections.actionPopularListFragmentToPersonDetailsFragment(
                item.id.value
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}