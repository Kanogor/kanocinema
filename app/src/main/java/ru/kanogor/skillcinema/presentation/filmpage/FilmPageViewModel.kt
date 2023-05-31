package ru.kanogor.skillcinema.presentation.filmpage

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.data.database.DataBaseRepository
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.database.SavedCollection
import ru.kanogor.skillcinema.data.entity.database.DefaultCollectionsName
import ru.kanogor.skillcinema.data.entity.database.NewSavedFilm
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryItems
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryPicture
import ru.kanogor.skillcinema.data.entity.retrofit.IdFilm
import ru.kanogor.skillcinema.data.entity.retrofit.SeasonsInfo
import ru.kanogor.skillcinema.data.entity.retrofit.Staff
import javax.inject.Inject

private const val FILM_VM = "FilmPageViewModel"

@HiltViewModel
class FilmPageViewModel @Inject constructor(
    private val repository: FilmRepository,
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    val collectionsName = DefaultCollectionsName()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _film = MutableStateFlow<IdFilm?>(null)
    val film = _film.asStateFlow()

    private val _actorsStaff = MutableStateFlow<List<Staff>>(emptyList())
    val actorsStaff = _actorsStaff.asStateFlow()

    private val _professionStaff = MutableStateFlow<List<Staff>>(emptyList())
    val professionStaff = _professionStaff.asStateFlow()

    private val _galleryPictures = MutableStateFlow<List<GalleryPicture>>(emptyList())
    val galleryPictures = _galleryPictures.asStateFlow()

    private val _similarFilms = MutableStateFlow<List<IdFilm>>(emptyList())
    val similarFilms = _similarFilms.asStateFlow()

    private val _seasonsInfo = MutableStateFlow<SeasonsInfo?>(null)
    val seasonsInfo = _seasonsInfo.asStateFlow()

    private val _galleryItemsCount = MutableStateFlow(0)
    val galleryItemsCount = _galleryItemsCount.asStateFlow()

    fun loadInfo(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            kotlin.runCatching {
                repository.getIdFilm(filmId)
            }.fold(
                onSuccess = {
                    _film.value = it
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "FilmPageFun")
                }
            )
            kotlin.runCatching {
                repository.getStaff(filmId)
            }.fold(
                onSuccess = { staffList ->
                    val actorsList = mutableListOf<Staff>()
                    val professionList = mutableListOf<Staff>()
                    staffList.onEach { staff ->
                        if (staff.professionKey == "ACTOR") actorsList.add(staff)
                        else professionList.add(staff)
                    }
                    _actorsStaff.value = actorsList
                    _professionStaff.value = professionList
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting staff unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getSimilarFilms(filmId)
            }.fold(
                onSuccess = { simFilmsInfo ->
                    val listOfFilms = mutableListOf<IdFilm>()
                    simFilmsInfo.items.onEach {
                        listOfFilms.add(repository.getIdFilm(it.filmId))
                    }
                    _similarFilms.value = listOfFilms
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting similar films unsuccessful")
                }
            )
            _isLoading.value = false
        }
    }

    fun loadSeasonsInfo(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                repository.getSeasonsInfo(filmId)
            }.fold(
                onSuccess = {
                    _seasonsInfo.value = it
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting seasons info unsuccessful")
                }
            )
        }
    }

    fun loadGalleryInfo(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                repository.getPosterPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getConceptPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getCoverPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getPromoPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getFanArtPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getScreenshotPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getShootingPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getStillPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
            kotlin.runCatching {
                repository.getWallPaperPictures(filmId, FIRST_PAGE)
            }.fold(
                onSuccess = { galleryItems ->
                    setGalleryItems(galleryItems)
                },
                onFailure = {
                    Log.d(FILM_VM, it.message ?: "Getting photo unsuccessful")
                }
            )
        }
    }

    fun refresh(filmId: Int) {
        loadInfo(filmId)
    }

    private fun setGalleryItems(galleryItems: GalleryItems) {
        val list = _galleryPictures.value.toMutableList()
        galleryItems.items.onEach {
            list.add(it)
        }
        _galleryPictures.value = list.toList()
        _galleryItemsCount.value = _galleryItemsCount.value + galleryItems.total
    }

    val savedCollections = this.dataBaseRepository.getCollection()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            savedCollections.collect {
                Log.d("DataBase", it.toString())
            }
        }
    }

    private var savedFilmLike: NewSavedFilm? = null
    private var savedFilmWatched: NewSavedFilm? = null
    private var savedFilmWantWatch: NewSavedFilm? = null
    private var savedFilmInterested: NewSavedFilm? = null

    private val _isFilmLiked = MutableStateFlow(false)
    val isFilmLiked = _isFilmLiked.asStateFlow()

    private val _isFilmWatched = MutableStateFlow(false)
    val isFilmWatched = _isFilmWatched.asStateFlow()

    private val _isFilmWantWatched = MutableStateFlow(false)
    val isFilmWantWatched = _isFilmWantWatched.asStateFlow()

    fun checkIcons(id: Int, name: String, genres: String, rating: Double?, url: String) {
        savedFilmLike = NewSavedFilm(
            collectionName = collectionsName.liked,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        savedFilmWatched = NewSavedFilm(
            collectionName = collectionsName.watched,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        savedFilmWantWatch = NewSavedFilm(
            collectionName = collectionsName.wantWatch,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        savedFilmInterested = NewSavedFilm(
            collectionName = collectionsName.interested,
            filmId = id,
            name = name,
            genres = genres,
            rating = rating,
            url = url
        )
        viewModelScope.launch {
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.liked) {
                    _isFilmLiked.value =
                        filmsCollection.collectionFilms!!.any { it.filmId == savedFilmLike!!.filmId && it.collectionName == collectionsName.liked }
                }
            }
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.watched) {
                    _isFilmWatched.value =
                        filmsCollection.collectionFilms!!.any { it.filmId == savedFilmWatched!!.filmId && it.collectionName == collectionsName.watched }
                }
            }
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.wantWatch) {
                    _isFilmWantWatched.value =
                        filmsCollection.collectionFilms!!.any { it.filmId == savedFilmWantWatch!!.filmId && it.collectionName == collectionsName.wantWatch }
                }
            }
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.interested) {
                    val isContain =
                        filmsCollection.collectionFilms!!.any { it.filmId == savedFilmInterested!!.filmId && it.collectionName == collectionsName.interested }
                    if (!isContain && savedFilmInterested!!.name != "null") {
                        dataBaseRepository.insertFilm(savedFilmInterested!!)
                    }
                }
            }
        }
    }

    fun onIconButtonClick(collectionName: String) {
        val savedFilm = when (collectionName) {
            collectionsName.liked -> savedFilmLike
            collectionsName.watched -> savedFilmWatched
            collectionsName.wantWatch -> savedFilmWantWatch
            else -> null
        }
        viewModelScope.launch {
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionName) {
                    if (filmsCollection.collectionFilms!!.any { it.filmId == savedFilm!!.filmId && it.collectionName == collectionName }) {
                        dataBaseRepository.deleteFilm(collectionName, savedFilm!!.filmId)
                        when (collectionName) {
                            collectionsName.liked -> _isFilmLiked.value = false
                            collectionsName.watched -> _isFilmWatched.value = false
                            collectionsName.wantWatch -> _isFilmWantWatched.value = false
                        }
                    } else {
                        dataBaseRepository.insertFilm(savedFilm!!)
                        when (collectionName) {
                            collectionsName.liked -> _isFilmLiked.value = true
                            collectionsName.watched -> _isFilmWatched.value = true
                            collectionsName.wantWatch -> _isFilmWantWatched.value = true
                        }
                    }
                }
            }
        }
    }

    fun checkFilm(
        collectionName: String,
        filmId: Int,
        title: String,
        genres: String,
        rating: Double?,
        url: String
    ) {
        val savedFilm = NewSavedFilm(
            collectionName = collectionName,
            filmId = filmId,
            name = title,
            genres = genres,
            rating = rating,
            url = url
        )
        viewModelScope.launch {
            savedCollections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionName) {
                    if (filmsCollection.collectionFilms!!.any { it.filmId == savedFilm.filmId && it.collectionName == collectionName }) {
                        dataBaseRepository.deleteFilm(collectionName, savedFilm.filmId)
                    } else {
                        dataBaseRepository.insertFilm(savedFilm)
                    }
                }
            }
        }
    }

    private val _collectionAdded = MutableStateFlow("")
    val collectionAdded = _collectionAdded.asStateFlow()
    fun addCollection(collectionName: String) {
        viewModelScope.launch {
            try {
                dataBaseRepository.insertCollection(SavedCollection(collectionName))
            } catch (e: SQLiteConstraintException) {
                _collectionAdded.value = collectionName
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}