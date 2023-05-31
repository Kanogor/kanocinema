package ru.kanogor.skillcinema.presentation.gallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryPicture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_concept.ConceptPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_cover.CoverPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_fanart.FanArtPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_poster.PosterPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_promo.PromoPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_screenshot.ScreenshotPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_shooting.ShootingPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_still.StillPicturesPagingSource
import ru.kanogor.skillcinema.presentation.adapters.gallery.paging_wallpaper.WallpaperPicturesPagingSource
import javax.inject.Inject

private const val GALLERY_VM = "gallery_viewModel"

@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: FilmRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Poster

    private val _posterItemsCount = MutableStateFlow<Int>(0)
    val posterItemsCount = _posterItemsCount.asStateFlow()

    var posterPicturesPagingSource: PosterPicturesPagingSource? = null

    fun loadPosterPictures(filmId: Int) {
        posterPicturesPagingSource = PosterPicturesPagingSource(repository, filmId)
    }

    val pagingPosterPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { posterPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)


    // Concept

    private val _conceptItemsCount = MutableStateFlow<Int>(0)
    val conceptItemsCount = _conceptItemsCount.asStateFlow()

    var conceptPicturesPagingSource: ConceptPicturesPagingSource? = null

    fun loadConceptPictures(filmId: Int) {
        conceptPicturesPagingSource = ConceptPicturesPagingSource(repository, filmId)
    }

    val pagingConceptPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { conceptPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // Cover

    private val _coverItemsCount = MutableStateFlow<Int>(0)
    val coverItemsCount = _coverItemsCount.asStateFlow()

    private var coverPicturesPagingSource: CoverPicturesPagingSource? = null

    fun loadCoverPictures(filmId: Int) {
        coverPicturesPagingSource = CoverPicturesPagingSource(repository, filmId)
    }

    val pagingCoverPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { coverPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // Promo

    private val _promoItemsCount = MutableStateFlow<Int>(0)
    val promoItemsCount = _promoItemsCount.asStateFlow()

    private var promoPicturesPagingSource: PromoPicturesPagingSource? = null

    fun loadPromoPictures(filmId: Int) {
        promoPicturesPagingSource = PromoPicturesPagingSource(repository, filmId)
    }

    val pagingPromoPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { promoPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // FanArt

    private val _fanArtItemsCount = MutableStateFlow<Int>(0)
    val fanArtItemsCount = _fanArtItemsCount.asStateFlow()

    private var fanArtPicturesPagingSource: FanArtPicturesPagingSource? = null

    fun loadFanArtPictures(filmId: Int) {
        fanArtPicturesPagingSource = FanArtPicturesPagingSource(repository, filmId)
    }

    val pagingFanArtPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { fanArtPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // Screenshot

    private val _screenshotItemsCount = MutableStateFlow<Int>(0)
    val screenshotItemsCount = _screenshotItemsCount.asStateFlow()

    private var screenshotPicturesPagingSource: ScreenshotPicturesPagingSource? = null

    fun loadScreenshotPictures(filmId: Int) {
        screenshotPicturesPagingSource = ScreenshotPicturesPagingSource(repository, filmId)
    }

    val pagingScreenshotPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { screenshotPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // Shooting

    private val _shootingItemsCount = MutableStateFlow<Int>(0)
    val shootingItemsCount = _shootingItemsCount.asStateFlow()

    private var shootingPicturesPagingSource: ShootingPicturesPagingSource? = null

    fun loadShootingPictures(filmId: Int) {
        shootingPicturesPagingSource = ShootingPicturesPagingSource(repository, filmId)
    }

    val pagingShootingPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { shootingPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // Still

    private val _stillItemsCount = MutableStateFlow<Int>(0)
    val stillItemsCount = _stillItemsCount.asStateFlow()

    private var stillPicturesPagingSource: StillPicturesPagingSource? = null

    fun loadStillPictures(filmId: Int) {
        stillPicturesPagingSource = StillPicturesPagingSource(repository, filmId)
    }

    val pagingStillPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { stillPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // Wallpaper

    private val _wallpaperItemsCount = MutableStateFlow<Int>(0)
    val wallpaperItemsCount = _wallpaperItemsCount.asStateFlow()

    private var wallpaperPicturesPagingSource: WallpaperPicturesPagingSource? = null

    fun loadWallpaperPictures(filmId: Int) {
        wallpaperPicturesPagingSource = WallpaperPicturesPagingSource(repository, filmId)
    }

    val pagingWallpaperPictures: Flow<PagingData<GalleryPicture>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { wallpaperPicturesPagingSource!! }
    ).flow.cachedIn(viewModelScope)

    // LoadInfo

    fun loadGalleryInfo(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            kotlin.runCatching {
                repository.getPosterPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _posterItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getConceptPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _conceptItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getCoverPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _coverItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getPromoPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _promoItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getFanArtPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _fanArtItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getScreenshotPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _screenshotItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getShootingPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _shootingItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getStillPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _stillItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getWallPaperPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    _wallpaperItemsCount.value = galleryItems.total
                },
                onFailure = {
                    Log.d(GALLERY_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            _isLoading.value = false
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }

}