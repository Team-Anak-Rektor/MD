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
) : PagingSource<Int, NearbySearchResult>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NearbySearchResult> {
        return try {
            val page = params.key ?: INIT_PAGE_INDEX
            val response = apiService.getNearbyRestaurant1(
                keyword = keyword,
                location = location
            ).results

            LoadResult.Page(
                data = response,
                prevKey = if (page == INIT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isNullOrEmpty()) null else page + 1
            )

        }catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NearbySearchResult>): Int? {
        return  state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INIT_PAGE_INDEX = 1
    }

}