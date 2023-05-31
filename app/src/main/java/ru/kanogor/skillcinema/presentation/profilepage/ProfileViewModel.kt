package ru.kanogor.skillcinema.presentation.profilepage

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kanogor.skillcinema.data.database.DataBaseRepository
import ru.kanogor.skillcinema.data.entity.database.SavedCollection
import ru.kanogor.skillcinema.data.entity.database.DefaultCollectionsName
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val dataBaseRepository: DataBaseRepository) :
    ViewModel() {

    val collectionsName = DefaultCollectionsName()

    val collections = this.dataBaseRepository.getCollection().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    fun deleteAllFilms(collectionName: String) {
        viewModelScope.launch {
            collections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionName) {
                    dataBaseRepository.deleteAllFilms(collectionName)
                }
            }
        }
    }

    fun deleteCollection(collectionName: String) {
        viewModelScope.launch {
            collections.value.forEach { filmsCollection ->
                if (filmsCollection.savedCollection.collectionName == collectionName) {
                    dataBaseRepository.deleteAllFilms(collectionName)
                    dataBaseRepository.deleteCollection(collectionName)
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

}