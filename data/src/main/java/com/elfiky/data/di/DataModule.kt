package com.elfiky.data.di

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.elfiky.data.network.Network
import com.elfiky.data.network.entities.PersonOverviewApi
import com.elfiky.data.network.intercepors.ApiKeyInterceptor
import com.elfiky.data.repository.person.PersonRepositoryImpl
import com.elfiky.data.repository.person.datasource.PersonRemoteDataSource
import com.elfiky.data.repository.person.datasource.PersonRemoteDataSourceImpl
import com.elfiky.data.repository.personoverview.PersonOverviewRepositoryImpl
import com.elfiky.data.repository.personoverview.datasource.PersonOverviewPagingSource
import com.elfiky.domain.entities.PersonOverview
import com.elfiky.domain.repository.PersonOverviewRepository
import com.elfiky.domain.repository.PersonRepository
import okhttp3.Interceptor
import org.koin.dsl.module
import retrofit2.Converter

private val networkModule = module {
    single<Interceptor> { ApiKeyInterceptor() }
    single { Network.okHttp(apiKeyInterceptor = get()) }
    single<Converter.Factory> { Network.moshiFactory() }
    single { Network.retrofit(client = get(), factory = get()) }
    single { Network.moviesService(retrofit = get()) }
}

private val repositoryModule = module {
    single<PagingSource<Int, PersonOverviewApi>> { PersonOverviewPagingSource(api = get()) }
    single<PersonOverviewRepository<PagingData<PersonOverview>>> { PersonOverviewRepositoryImpl(pagingSource = get()) }

    single<PersonRemoteDataSource> { PersonRemoteDataSourceImpl(api = get()) }
    single<PersonRepository> { PersonRepositoryImpl(remoteDataSource = get()) }
}

val dataModule = listOf(
    networkModule,
    repositoryModule
)