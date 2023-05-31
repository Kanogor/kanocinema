package ru.kanogor.skillcinema.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.kanogor.skillcinema.data.entity.database.SavedCollection
import ru.kanogor.skillcinema.data.entity.database.FilmsCollection
import ru.kanogor.skillcinema.data.entity.database.NewSavedFilm
import ru.kanogor.skillcinema.data.entity.database.SavedFilm

@Dao
interface SavedFilmDao {

    @Transaction
    @Query("SELECT * FROM savedCollection")
    fun getCollection(): Flow<List<FilmsCollection>>

    @Insert(entity = SavedFilm::class)
    suspend fun insertFilm(film: NewSavedFilm)

    @Insert
    suspend fun insertCollection(savedCollection: SavedCollection)

    @Query("DELETE FROM saved_films WHERE collection_name = :collectionName AND film_id = :filmId")
    suspend fun deleteFilm(collectionName: String, filmId: Int)

    @Query("DELETE FROM saved_films WHERE collection_name = :collectionName")
    suspend fun deleteAllFilms(collectionName: String)

    @Query("DELETE FROM savedCollection WHERE collection_name = :collectionName")
    suspend fun deleteCollection(collectionName: String)

}