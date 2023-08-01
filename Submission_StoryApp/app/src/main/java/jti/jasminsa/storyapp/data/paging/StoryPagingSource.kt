package jti.jasminsa.storyapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import jti.jasminsa.storyapp.data.response.DaftarCeritaResponse
import jti.jasminsa.storyapp.data.response.ListStoryItem
import jti.jasminsa.storyapp.data.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService, private val token: String) :
    PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        Log.e("TAGg", "${token}")
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoryPaging("Bearer $token",page)

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}