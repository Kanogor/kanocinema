package ru.kanogor.skillcinema.presentation.filmography

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.retrofit.PersonInfo
import ru.kanogor.skillcinema.data.entity.retrofit.StaffFilms
import javax.inject.Inject

private const val FILMOGRAPHY_VM = "FilmographyViewModel"

@HiltViewModel
class FilmographyViewModel @Inject constructor(private val repository: FilmRepository): ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _asWriterFilms = MutableStateFlow<List<StaffFilms>>(emptyList())
    val asWriterFilms = _asWriterFilms.asStateFlow()

    private val _asDirectorFilms = MutableStateFlow<List<StaffFilms>>(emptyList())
    val asDirectorFilms = _asDirectorFilms.asStateFlow()

    private val _asProducerFilms = MutableStateFlow<List<StaffFilms>>(emptyList())
    val asProducerFilms = _asProducerFilms.asStateFlow()

    private val _asActorFilms = MutableStateFlow<List<StaffFilms>>(emptyList())
    val asActorFilms = _asActorFilms.asStateFlow()

    private val _asHimselfFilms = MutableStateFlow<List<StaffFilms>>(emptyList())
    val asHimselfFilms = _asHimselfFilms.asStateFlow()

    private val _otherFilms = MutableStateFlow<List<StaffFilms>>(emptyList())
    val otherFilms = _otherFilms.asStateFlow()

    private val _personInfo = MutableStateFlow<PersonInfo?>(null)
    val personInfo = _personInfo.asStateFlow()

    private val _isItMan = MutableStateFlow<Boolean>(true)
    val isItMan  = _isItMan.asStateFlow()

    fun loadInfo(personId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            kotlin.runCatching {
                repository.getPersonInfo(personId)
            }.fold(
                onSuccess = { info ->
                    _personInfo.value = info
                    _isItMan.value = if(info.sex == "FEMALE") false else true
                    val listOfFilms = info.films.toMutableList()
                    val writer = mutableListOf<StaffFilms>()
                    val director = mutableListOf<StaffFilms>()
                    val producer =  mutableListOf<StaffFilms>()
                    val actor = mutableListOf<StaffFilms>()
                    val himself = mutableListOf<StaffFilms>()
                    val other = mutableListOf<StaffFilms>()
                    listOfFilms.onEach { film ->
                        when(film.professionKey) {
                            "WRITER" -> writer.add(film)
                            "DIRECTOR" -> director.add(film)
                            "PRODUCER" -> producer.add(film)
                            "ACTOR" -> actor.add(film)
                            "HIMSELF" -> himself.add(film)
                            else -> other.add(film)
                        }
                    }
                    _asWriterFilms.value = writer
                    _asDirectorFilms.value = director
                    _asProducerFilms.value = producer
                    _asActorFilms.value = actor
                    _asHimselfFilms.value = himself
                    _otherFilms.value = other
                },
                onFailure = {
                    Log.d(FILMOGRAPHY_VM, it.message ?: "Getting film list unsuccessful")
                }
            )
            _isLoading.value = false
        }
    }

}