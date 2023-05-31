package ru.kanogor.skillcinema.data.retrofit.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.skillcinema.data.entity.retrofit.Episode
import ru.kanogor.skillcinema.data.entity.retrofit.Season
import ru.kanogor.skillcinema.data.entity.retrofit.SeasonsInfo

@JsonClass(generateAdapter = true)
data class SeasonsInfoDto(
    @Json(name = "total")
    override val total: Int,
    @Json(name = "items")
    override val items: List<ru.kanogor.skillcinema.data.retrofit.dto.Season>
) : SeasonsInfo

@JsonClass(generateAdapter = true)
data class Season(
    @Json(name = "number")
    override val number: Int,
    @Json(name = "episodes")
    override val episodes: List<ru.kanogor.skillcinema.data.retrofit.dto.Episode>
) : Season

@JsonClass(generateAdapter = true)
data class Episode(
    @Json(name = "seasonNumber")
    override val seasonNumber: Int,
    @Json(name = "episodeNumber")
    override val episodeNumber: Int,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "releaseDate")
    override val releaseDate: String?
) : Episode