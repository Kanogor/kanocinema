package ru.kanogor.skillcinema.presentation.adapters.topfilms

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.retrofit.TopFilmsList

class TopFilmsPagingSource(private val repository: FilmRepository) :
    PagingSource<Int, TopFilmsList>() {

    override fun getRefreshKey(state: PagingState<Int, TopFilmsList>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TopFilmsList> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getTopFilms(page)
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