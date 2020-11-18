package com.elfiky.ibtikarandroidtask.di

import android.app.DownloadManager
import android.content.Context
import com.elfiky.ibtikarandroidtask.persondetails.PersonDetailsViewModel
import com.elfiky.ibtikarandroidtask.popularlist.PopularListViewModel
import com.elfiky.ibtikarandroidtask.receiver.OnDownloadCompleteReceiver
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { PopularListViewModel(get()) }
    viewModel { (id: Int) -> PersonDetailsViewModel(id, get()) }

    factory { get<Context>().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }
    factory { OnDownloadCompleteReceiver(get()) }
}