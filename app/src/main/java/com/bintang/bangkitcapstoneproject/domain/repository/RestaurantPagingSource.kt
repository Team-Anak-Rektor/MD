package com.bintang.bangkitcapstoneproject.domain.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bintang.bangkitcapstoneproject.model.NearbySearchResult
import com.bintang.bangkitcapstoneproject.network.GooglePlaceApiService
import retrofit2.HttpException
import java.io.IOException

class RestaurantPagingSource(
    private val apiService: GooglePlaceApiService,
    private val keyword: String,
    private val location: String
) : PagingSource<String, NearbySearchResult>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, NearbySearchResult> {
        val pageToken = params.key ?: INIT_PAGE_TOKEN
        return try {
            val response = apiService.getNearbyRestaurant(
                keyword = keyword,
                location = location,
                pageToken = pageToken
            )
            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = response.nextPageToken
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<String, NearbySearchResult>): String? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.placeId }
    }

    private companion object {
        const val INIT_PAGE_TOKEN = ""
    }
}