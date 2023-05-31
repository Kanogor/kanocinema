package ru.kanogor.skillcinema.data.retrofit.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.skillcinema.data.entity.retrofit.IdFilm

@JsonClass(generateAdapter = true)
data class IdFilmDto(
    @Json(name = "kinopoiskId")
    override val kinopoiskId: Int,
    @Json(name = "imdbId")
    override val imdbId: String?,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String,
    @Json(name = "ratingKinopoisk")
    override val ratingKinopoisk: Double?,
    @Json(name = "countries")
    override val countries: List<Country>,
    @Json(name = "genres")
    override val genres: List<Genre>,
    @Json(name = "description")
    override val description: String?,
    @Json(name = "year")
    override val year: String?,
    @Json(name = "filmLength")
    override val filmLength: Int?,
    @Json(name = "ratingAwait")
    override val ratingAwait: Double?,
    @Json(name = "ratingImdb")
    override val ratingImdb: Double?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "nameOriginal")
    override val nameOriginal: String?,
    @Json(name = "coverUrl")
    override val coverUrl: String?,
    @Json(name = "logoUrl")
    override val logoUrl: String?,
    @Json(name = "ratingAgeLimits")
    override val ratingAgeLimits: String?,
    @Json(name = "shortDescription")
    override val shortDescription: String?,
    @Json(name = "serial")
    override val serial: Boolean
) : IdFilm