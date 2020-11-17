package com.elfiky.ibtikarandroidtask.popularlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.elfiky.ibtikarandroidtask.databinding.PopularListFragmentBinding

class PopularListFragment : Fragment() {

    private var _binding: PopularListFragmentBinding? = null
    private val binding get() = _binding!!

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

        binding.details.setOnClickListener {
            findNavController().navigate(PopularListFragmentDirections.actionPopularListFragmentToPersonDetailsFragment(1))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}