package ru.kanogor.skillcinema.data.retrofit.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.skillcinema.data.entity.retrofit.FilteredFilms
import ru.kanogor.skillcinema.data.entity.retrofit.FilteredFilmsList

@JsonClass(generateAdapter = true)
data class FilteredFilmsDto(
    @Json(name = "total")
    override val total: Int,
    @Json(name = "totalPages")
    override val totalPages: Int,
    @Json(name = "items")
    override val items: List<ru.kanogor.skillcinema.data.retrofit.dto.FilteredFilmsList>?
) : FilteredFilms

@JsonClass(generateAdapter = true)
data class FilteredFilmsList(
    @Json(name = "kinopoiskId")
    override val kinopoiskId: Int,
    @Json(name = "imdbId")
    override val imdbId: String?,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "nameOriginal")
    override val nameOriginal: String?,
    @Json(name = "countries")
    override val countries: List<Country>,
    @Json(name = "genres")
    override val genres: List<Genre>,
    @Json(name = "ratingKinopoisk")
    override val ratingKinopoisk: Double?,
    @Json(name = "ratingImdb")
    override val ratingImdb: Double?,
    @Json(name = "year")
    override val year: Int?,
    @Json(name = "type")
    override val type: String,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String
) : FilteredFilmsList