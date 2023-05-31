package ru.kanogor.skillcinema.data.retrofit.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryItems
import ru.kanogor.skillcinema.data.entity.retrofit.GalleryPicture

@JsonClass(generateAdapter = true)
data class GalleryItemsDto(
    @Json(name = "total")
    override val total: Int,
    @Json(name = "totalPages")
    override val totalPages: Int,
    @Json(name = "items")
    override val items: List<ru.kanogor.skillcinema.data.retrofit.dto.GalleryPicture>
) : GalleryItems

@JsonClass(generateAdapter = true)
data class GalleryPicture(
    @Json(name = "imageUrl")
    override val imageUrl: String,
    @Json(name = "previewUrl")
    override val previewUrl: String
) : GalleryPicture