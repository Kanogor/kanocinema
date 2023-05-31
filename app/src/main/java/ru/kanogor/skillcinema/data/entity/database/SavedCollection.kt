package ru.kanogor.skillcinema.data.entity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savedCollection")
data class SavedCollection(
    @PrimaryKey
    @ColumnInfo(name = "collection_name")
    val collectionName: String
)
