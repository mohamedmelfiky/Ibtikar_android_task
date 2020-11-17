package com.elfiky.domain.repository

import kotlinx.coroutines.flow.Flow

interface PersonOverviewRepository<out T> {
    fun getPopularStream(): Flow<T>
}