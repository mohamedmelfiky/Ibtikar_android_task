package com.elfiky.ibtikarandroidtask.persondetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.elfiky.domain.entities.InvalidApiKey
import com.elfiky.domain.entities.NetworkFailure
import com.elfiky.domain.entities.NotFound
import com.elfiky.domain.entities.ProfileImage
import com.elfiky.domain.entities.UnknownFailure
import com.elfiky.ibtikarandroidtask.R
import com.elfiky.ibtikarandroidtask.databinding.PersonDetailsFragmentBinding
import com.elfiky.ibtikarandroidtask.delegates.viewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PersonDetailsFragment : Fragment(R.layout.person_details_fragment) {

    private val binding by viewBinding(PersonDetailsFragmentBinding::bind)
    private val args: PersonDetailsFragmentArgs by navArgs()
    private val viewModel: PersonDetailsViewModel by viewModel { parametersOf(args.id) }
    private val adapter = ProfileImageAdapter(onClick = ::onItemClicked)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            imagesRv.setHasFixedSize(true)
            imagesRv.adapter = adapter

            backImg.setOnClickListener { findNavController().navigateUp() }
            personNameTv.text = args.name
            retryBtn.setOnClickListener { viewModel.getPersonDetails() }
        }

        viewModel.state
            .onEach { state -> render(state) }
            .launchIn(lifecycleScope)
    }

    private fun render(state: PersonDetailsState) {
        when (state) {
            is PersonDetailsState.Loading -> showLoading()
            is PersonDetailsState.Success -> showSuccess(state)
            is PersonDetailsState.Error -> showError(state)
        }
    }

    private fun showLoading() {
        binding.loadingPr.isVisible = true
        binding.successCl.isVisible = false
        binding.errorCl.isVisible = false
    }

    private fun showError(state: PersonDetailsState.Error) {
        binding.loadingPr.isVisible = false
        binding.successCl.isVisible = false
        binding.errorCl.isVisible = true

        binding.errorTv.text = when (state.failure) {
            NetworkFailure -> getString(R.string.error_network)
            InvalidApiKey -> getString(R.string.error_invalid_api_key)
            NotFound -> getString(R.string.error_not_found)
            UnknownFailure -> getString(R.string.error_unknown)
        }
    }

    private fun showSuccess(state: PersonDetailsState.Success) {
        binding.loadingPr.isVisible = false
        binding.successCl.isVisible = true
        binding.errorCl.isVisible = false

        adapter.submitList(state.person.images)

        val details = state.person.details
        with(binding) {
            personImg.load(details.thumbnail.url) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
                placeholder(R.drawable.image_place_holder)
            }
            knownForValueTv.text = details.department.name
            genderValueTv.text = details.gender.name
            birthdayValueTv.text = details.birthday?.date ?: getString(R.string.unknown)
            placeOfBirthValueTv.text = details.placeOfBirth?.name ?: getString(R.string.unknown)
            alsoKnownAsValueTv.text =
                details.otherKnownNames.joinToString(", ") { it.value }.ifEmpty {
                    getString(R.string.no_other_names)
                }
            biographyValueTv.text = details.biography.value
        }
    }

    private fun onItemClicked(profileImage: ProfileImage) {
        findNavController().navigate(
            PersonDetailsFragmentDirections.actionPersonDetailsFragmentToImageViewerFragment(
                originalUrl = profileImage.original.url,
                name = args.name
            )
        )
    }
}