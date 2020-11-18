package com.elfiky.ibtikarandroidtask.di

import com.elfiky.ibtikarandroidtask.popularlist.PopularListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { PopularListViewModel(get()) }
}