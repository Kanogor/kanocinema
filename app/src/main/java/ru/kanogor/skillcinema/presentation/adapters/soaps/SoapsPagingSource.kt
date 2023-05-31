package ru.kanogor.skillcinema.presentation.adapters.soaps

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.retrofit.FilteredFilmsList

class SoapsPagingSource(private val repository: FilmRepository) : PagingSource<Int, FilteredFilmsList>() {

    override fun getRefreshKey(state: PagingState<Int, FilteredFilmsList>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FilteredFilmsList> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getSoaps(page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { LoadResult.Error(it) }
        )
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}