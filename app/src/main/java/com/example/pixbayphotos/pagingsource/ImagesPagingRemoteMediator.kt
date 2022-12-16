package com.example.pixbayphotos.pagingsource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pixbayphotos.network.ApiService
import com.example.pixbayphotos.db.AppDatabase
import com.example.pixbayphotos.models.Image
import com.example.pixbayphotos.models.RemoteKeys
import okio.IOException
import retrofit2.HttpException

private const val DEFAULT_PAGE = 1


@OptIn(ExperimentalPagingApi::class)
class ImagesPagingRemoteMediator constructor(
    private val apiKey: String,
    private val query: String,
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, Image>() {
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, Image>
    ): MediatorResult {

        var loadKey = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = appDatabase.withTransaction {
                    appDatabase.keysDao().getKeys().firstOrNull()
                }
                if (remoteKey?.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                remoteKey.nextKey
            }
        }

        loadKey = loadKey ?: DEFAULT_PAGE

        try {
            val response = apiService.fetchImages(
                key = apiKey,
                page = loadKey,
                pageSize = state.config.pageSize,
                searchTerm = query
            )
            val images =
                if (response.isSuccessful) response.body()?.images ?: emptyList() else emptyList()
            val isEndOfList = images.isEmpty()
            appDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    appDatabase.keysDao().clearRemoteKeys()
                    appDatabase.imagesDao().clearAll()
                }
                val prevKey = if (loadKey == DEFAULT_PAGE) null else loadKey - 1
                val nextKey = if (isEndOfList) null else loadKey + 1
                appDatabase.imagesDao().insertImages(images)
                val dbImages = appDatabase.imagesDao().getImagesNormal()
                val keys = dbImages.map {
                    RemoteKeys(repoId = it.pk, prevKey = prevKey, nextKey = nextKey)
                }
                appDatabase.keysDao().insertAll(keys)

            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

}