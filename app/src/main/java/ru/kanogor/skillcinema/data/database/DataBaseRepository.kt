package ru.kanogor.skillcinema.data.database

import ru.kanogor.skillcinema.data.entity.database.SavedCollection
import ru.kanogor.skillcinema.data.entity.database.NewSavedFilm
import javax.inject.Inject

class DataBaseRepository @Inject constructor(private val savedFilmDao: SavedFilmDao) {

    fun getCollection() = savedFilmDao.getCollection()

    suspend fun insertFilm(film: NewSavedFilm) = savedFilmDao.insertFilm(film)

    suspend fun insertCollection(savedCollection: SavedCollection) =
        savedFilmDao.insertCollection(savedCollection)

    suspend fun deleteFilm(collectionName: String, filmId: Int) =
        savedFilmDao.deleteFilm(collectionName, filmId)

    suspend fun deleteAllFilms(collectionName: String) = savedFilmDao.deleteAllFilms(collectionName)

    suspend fun deleteCollection(collectionName: String) =
        savedFilmDao.deleteCollection(collectionName)

}