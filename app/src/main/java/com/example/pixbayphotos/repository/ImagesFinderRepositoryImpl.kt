package com.example.pixbayphotos.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.pixbayphotos.network.ApiService
import com.example.pixbayphotos.db.AppDatabase
import com.example.pixbayphotos.di.Constants
import com.example.pixbayphotos.models.Image
import com.example.pixbayphotos.pagingsource.ImagesPagingRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class ImagesFinderRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    @Named(Constants.KEY_CONSTANT) private val key: String,
    private val appDatabase: AppDatabase
) {


    @OptIn(ExperimentalPagingApi::class)
    fun getSearchImages(query: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 2
            ),
            remoteMediator = ImagesPagingRemoteMediator(
                apiKey = key,
                query = query,
                apiService = apiService,
                appDatabase = appDatabase
            ),
            pagingSourceFactory = {
                appDatabase.imagesDao().getImagesPaging()
            }
        ).flow
    }
}