package ru.kanogor.skillcinema.presentation.homepage

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.data.database.DataBaseRepository
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.database.DefaultCollectionsName
import ru.kanogor.skillcinema.data.entity.database.SavedCollection
import ru.kanogor.skillcinema.data.entity.retrofit.FilteredFilmsList
import ru.kanogor.skillcinema.data.entity.retrofit.PremieresFilmsList
import ru.kanogor.skillcinema.data.entity.retrofit.TopFilmsList
import ru.kanogor.skillcinema.presentation.adapters.popularfilms.PopularFilmsPagingSource
import ru.kanogor.skillcinema.presentation.adapters.randomfilms.FirstRandomFilmsPagingSource
import ru.kanogor.skillcinema.presentation.adapters.randomfilms.SecondRandomFilmsPagingSource
import ru.kanogor.skillcinema.presentation.adapters.soaps.SoapsPagingSource
import ru.kanogor.skillcinema.presentation.adapters.topfilms.TopFilmsPagingSource
import javax.inject.Inject

private const val HOME_VM = "HomepageViewModel"

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val repository: FilmRepository,
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    private val collections = this.dataBaseRepository.getCollection().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _firstRandomFilmTitle = MutableStateFlow("")
    val firstRandomFilmTitle = _firstRandomFilmTitle.asStateFlow()

    private val _firstRandomFilms = MutableStateFlow<List<FilteredFilmsList>>(emptyList())
    val firstRandomFilms = _firstRandomFilms.asStateFlow()

    private val _secondRandomFilmTitle = MutableStateFlow("")
    val secondRandomFilmTitle = _secondRandomFilmTitle.asStateFlow()

    private val _secondRandomFilms = MutableStateFlow<List<FilteredFilmsList>>(emptyList())
    val secondRandomFilms = _secondRandomFilms.asStateFlow()

    private val _popularMovieList = MutableStateFlow<List<TopFilmsList>>(emptyList())
    val popularMovieList = _popularMovieList.asStateFlow()

    private val _topMovieList = MutableStateFlow<List<TopFilmsList>>(emptyList())
    val topMovieList = _topMovieList.asStateFlow()

    private val _premieresMovieList = MutableStateFlow<List<PremieresFilmsList>>(emptyList())
    val premieresMovieList = _premieresMovieList.asStateFlow()

    private val _soapsFilms = MutableStateFlow<List<FilteredFilmsList>>(emptyList())
    val soapsFilms = _soapsFilms.asStateFlow()

    val pagingTopFilms: Flow<PagingData<TopFilmsList>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { TopFilmsPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)

    val pagingPopularFilms: Flow<PagingData<TopFilmsList>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { PopularFilmsPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)

    val pagingSoaps: Flow<PagingData<FilteredFilmsList>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { SoapsPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)

    val pagingFirstRandomFilms: Flow<PagingData<FilteredFilmsList>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { FirstRandomFilmsPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)

    val pagingSecondRandomFilms: Flow<PagingData<FilteredFilmsList>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { SecondRandomFilmsPagingSource(repository) }
    ).flow.cachedIn(viewModelScope)

    private val collectionsName = DefaultCollectionsName()
    private fun addDefaultCollections() {
        viewModelScope.launch {
            try {
                dataBaseRepository.insertCollection(SavedCollection(collectionsName.liked))
            } catch (e: SQLiteConstraintException) {
                Log.d("DataBase", "Коллекция  ${collectionsName.liked} уже создана")
            }
            try {
                dataBaseRepository.insertCollection(SavedCollection(collectionsName.watched))
            } catch (e: SQLiteConstraintException) {
                Log.d("DataBase", "Коллекция  ${collectionsName.watched} уже создана")
            }
            try {
                dataBaseRepository.insertCollection(SavedCollection(collectionsName.wantWatch))
            } catch (e: SQLiteConstraintException) {
                Log.d("DataBase", "Коллекция  ${collectionsName.wantWatch} уже создана")
            }
            try {
                dataBaseRepository.insertCollection(SavedCollection(collectionsName.interested))
            } catch (e: SQLiteConstraintException) {
                Log.d("DataBase", "Коллекция  ${collectionsName.interested} уже создана")
            }
        }
    }

    init {
        addDefaultCollections()
        loadFilms()
        loadRandomFilm(
            randomCountry = repository.firstRandomCountry,
            randomGenre = repository.firstRandomGenre,
            titleStateFlow = _firstRandomFilmTitle,
            filmsStateFlow = _firstRandomFilms
        )
        loadRandomFilm(
            randomCountry = repository.secondRandomCountry,
            randomGenre = repository.secondRandomGenre,
            titleStateFlow = _secondRandomFilmTitle,
            filmsStateFlow = _secondRandomFilms
        )
        viewModelScope.launch {
            collections.collect {
                Log.d("DataBase", it.toString())
            }
        }
    }

    private fun loadRandomFilm(
        randomCountry: List<Int>,
        randomGenre: List<Int>,
        titleStateFlow: MutableStateFlow<String>,
        filmsStateFlow: MutableStateFlow<List<FilteredFilmsList>>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                repository.getRandomFilms(
                    randomCountry = randomCountry,
                    randomGenre = randomGenre,
                    page = FIRST_PAGE
                )
            }.fold(
                onSuccess = { filmsList ->
                    filmsStateFlow.value = filmsList
                },
                onFailure = { Log.d(HOME_VM, it.message ?: "FirstRandomFilms") }
            )
            kotlin.runCatching {
                repository.getFirstRandomFilmsTitle(
                    randomCountry = randomCountry,
                    randomGenre = randomGenre,
                )
            }.fold(
                onSuccess = { title ->
                    titleStateFlow.value = title
                },
                onFailure = { Log.d(HOME_VM, it.message ?: "FirstRandomFilmsTitle") }
            )
        }
    }

    private fun loadFilms() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            kotlin.runCatching {
                repository.getPopularFilms(FIRST_PAGE)
            }.fold(
                onSuccess = { filmsList ->
                    _popularMovieList.value = filmsList
                },
                onFailure = { Log.d(HOME_VM, it.message ?: "PopFilms") }
            )
            kotlin.runCatching {
                repository.getTopFilms(FIRST_PAGE)
            }.fold(
                onSuccess = { filmsList ->
                    _topMovieList.value = filmsList
                },
                onFailure = { Log.d(HOME_VM, it.message ?: "TopFilms") }
            )
            kotlin.runCatching {
                repository.getPremieresFilm()
            }.fold(
                onSuccess = { premieresFilms ->
                    _premieresMovieList.value = premieresFilms
                },
                onFailure = { Log.d(HOME_VM, it.message ?: "PremieresFilms") }
            )
            kotlin.runCatching {
                repository.getSoapsCode(FIRST_PAGE)
            }.fold(
                onSuccess = { Log.d(HOME_VM, it.toString()) },
                onFailure = { Log.d(HOME_VM, it.message ?: "Random") }
            )
            kotlin.runCatching {
                repository.getSoaps(FIRST_PAGE)
            }.fold(
                onSuccess = { filmList ->
                    _soapsFilms.value = filmList
                },
                onFailure = { Log.d(HOME_VM, it.message ?: "Soap") }
            )
            _isLoading.value = false
        }
    }

    private val _filmsIds = MutableStateFlow<List<Int>>(emptyList())
    val filmsIds = _filmsIds.asStateFlow()
    fun checkCollection() {
        viewModelScope.launch {
            collections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionsName.watched) {
                    val listIds = filmsCollection.collectionFilms?.map { it.filmId }
                    _filmsIds.value = listIds ?: emptyList()
                }
            }
        }
    }

    fun refresh() {
        loadFilms()
    }

    companion object {
        private const val FIRST_PAGE = 1
    }
}