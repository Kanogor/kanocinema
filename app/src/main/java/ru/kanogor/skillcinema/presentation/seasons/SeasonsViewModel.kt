package ru.kanogor.skillcinema.presentation.seasons

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.data.retrofit.FilmRepository
import ru.kanogor.skillcinema.data.entity.retrofit.SeasonsInfo
import javax.inject.Inject

private const val SEASONS_VW = "Seasons_ViewModel"

@HiltViewModel
class SeasonsViewModel @Inject constructor(private val repository: FilmRepository) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _seasonsInfo = MutableStateFlow<SeasonsInfo?>(null)
    val seasonsInfo = _seasonsInfo.asStateFlow()

    fun loadInfo(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            kotlin.runCatching {
                repository.getSeasonsInfo(filmId)
            }.fold(
                onSuccess = {
                    _seasonsInfo.value = it
                },
                onFailure = {
                    Log.d(SEASONS_VW, it.message ?: "Getting seasonsInfo unsuccessful")
                }
            )
            _isLoading.value = false
        }
    }
}