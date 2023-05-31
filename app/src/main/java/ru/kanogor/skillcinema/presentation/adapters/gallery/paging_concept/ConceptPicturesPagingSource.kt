package ru.kanogor.skillcinema.presentation.adapters.gallery.paging_concept

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryPicture

class ConceptPicturesPagingSource(private val repository: FilmRepository, val filmId: Int) :
    PagingSource<Int, GalleryPicture>() {

    override fun getRefreshKey(state: PagingState<Int, GalleryPicture>): Int = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryPicture> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getConceptPictures(filmId, page)
        }.fold(
            onSuccess = {
                LoadResult.Page(
                    data = it.items,
                    prevKey = null,
                    nextKey = if (it.items.isEmpty()) null else page + 1
                )
            },
            onFailure = {
                LoadResult.Error(it)
            }
        )
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}