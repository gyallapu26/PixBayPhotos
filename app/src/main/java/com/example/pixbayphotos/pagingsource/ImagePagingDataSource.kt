package com.example.pixbayphotos.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pixbayphotos.models.Image
import com.example.pixbayphotos.network.ApiService
import retrofit2.HttpException
import java.io.IOException

private const val DEFAULT_PAGE = 1


class ImagePagingDataSource constructor(
    private val apiKey: String,
    private val query: String,
    private val apiService: ApiService
) : PagingSource<Int, Image>() {

    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {

        val position = params.key ?: DEFAULT_PAGE
        return try {
            val response = apiService.fetchImages(key = apiKey, page = position, pageSize = params.loadSize, searchTerm = query )
             if (response.isSuccessful) {
                val images = response.body()?.images ?: emptyList()
                LoadResult.Page(
                    data = images,
                    prevKey = if (position == DEFAULT_PAGE) null else position - 1,
                    nextKey = if (images.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Throwable(response.errorBody()?.toString()))
            }
        } catch (exception: IOException) {
             LoadResult.Error(exception)
        } catch (exception: HttpException) {
             LoadResult.Error(exception)
        }
    }
}