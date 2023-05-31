package ru.kanogor.skillcinema.presentation.actorpage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.retrofit.IdFilm
import ru.kanogor.skillcinema.data.entity.retrofit.PersonInfo
import javax.inject.Inject

private const val ACTOR_PAGE_VM = "ActorPageViewModel"

@HiltViewModel
class ActorPageViewModel @Inject constructor(private val repository: FilmRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _bestFilms = MutableStateFlow<List<IdFilm>>(emptyList())
    val bestFilms = _bestFilms.asStateFlow()

    private val _personInfo = MutableStateFlow<PersonInfo?>(null)
    val personInfo = _personInfo.asStateFlow()

    fun loadInfo(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            kotlin.runCatching {
                repository.getPersonInfo(personId)
            }.fold(
                onSuccess = { info ->
                    _personInfo.value = info
                    val listOfFilms = info.films.toMutableList()
                    listOfFilms.onEach {
                        it.rating?.toDouble()
                    }
                    listOfFilms.sortBy { it.rating }
                    val distinctList = listOfFilms.distinctBy { it.filmId }
                    val sortedList =
                        if (distinctList.size > 20) distinctList.takeLast(20).reversed()
                        else distinctList.reversed() //манипуляции по сокращению списка до 20 позиций с самым высоким рейтингом
                    val bestFilmList = mutableListOf<IdFilm>()
                    sortedList.onEach {
                        bestFilmList.add(repository.getIdFilm(it.filmId))
                    }
                    _bestFilms.value = bestFilmList
                },
                onFailure = {
                    Log.d(ACTOR_PAGE_VM, it.message ?: "Getting Person Info unsuccessful")
                }
            )
            _isLoading.value = false
        }
    }
}