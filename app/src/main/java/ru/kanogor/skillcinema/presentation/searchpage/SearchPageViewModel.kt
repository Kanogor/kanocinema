package ru.kanogor.skillcinema.presentation.searchpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.data.database.DataBaseRepository
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.database.DefaultCollectionsName
import ru.kanogor.skillcinema.data.entity.retrofit.FilteredFilmsList
import ru.kanogor.skillcinema.data.entity.filter.CountryAndGenreLists
import ru.kanogor.skillcinema.data.entity.filter.FilmType
import ru.kanogor.skillcinema.data.entity.filter.FilmsOrder
import javax.inject.Inject

private const val SEARCH_VM = "SearchViewModel"

@HiltViewModel
class SearchPageViewModel @Inject constructor(
    val repository: FilmRepository,
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    val filmType = FilmType()
    val filmsOrder = FilmsOrder()
    val countryAndGenreLists = CountryAndGenreLists()

    var orderFilter = filmsOrder.year
    var countryFilter = countryAndGenreLists.countryList[0]
    var genreFilter = countryAndGenreLists.genreList[0]
    var yearFromFilter: Int? = 1970
    var yearToFilter: Int? = 2025
    var ratingFromFilter = 1
    var ratingToFilter = 10

    var typeFilter = filmType.allTypes

    private val collectionsName = DefaultCollectionsName()

    val collections = this.dataBaseRepository.getCollection().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            collections.collect {
                Log.d("DataBase", it.toString())
            }
        }
    }

    var isViewed = false

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _searchedFilms = MutableStateFlow<List<FilteredFilmsList>>(emptyList())
    val searchedFilms = _searchedFilms.asStateFlow()

    fun getFilms(
        text: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            delay(1000)
            kotlin.runCatching {
                repository.getSearch(
                    countries = countryFilter,
                    genres = genreFilter,
                    type = typeFilter,
                    order = orderFilter,
                    ratingFrom = ratingFromFilter,
                    ratingTo = ratingToFilter,
                    yearFrom = yearFromFilter!!,
                    yearTo = yearToFilter!!,
                    keyword = text,
                    page = FIRST_PAGE
                )
            }.fold(
                onSuccess = { filmsList ->
                    var resultList = mutableListOf<FilteredFilmsList>()
                    collections.value.forEach { filmsCollection ->
                        if (filmsCollection.savedCollection.collectionName == collectionsName.watched) {
                            val listId = filmsCollection.collectionFilms?.map { it.filmId }
                            if (isViewed) {
                                filmsList.forEach { film ->
                                    if (listId!!.contains(film.kinopoiskId)) resultList.add(film)
                                }
                            } else {
                                resultList = filmsList.toMutableList()
                                filmsList.forEach { film ->
                                    if (listId!!.contains(film.kinopoiskId)) resultList.remove(film)
                                }
                            }
                        }
                    }
                    _searchedFilms.value = resultList
                },
                onFailure = { Log.d(SEARCH_VM, it.message ?: "Search error") }
            )
            _isLoading.value = false
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }

}