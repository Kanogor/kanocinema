package ru.kanogor.skillcinema.data.retrofit.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.skillcinema.data.entity.retrofit.Country
import ru.kanogor.skillcinema.data.entity.retrofit.Genre
import ru.kanogor.skillcinema.data.entity.retrofit.TopFilms
import ru.kanogor.skillcinema.data.entity.retrofit.TopFilmsList

@JsonClass(generateAdapter = true)
data class TopFilmsDto(
    @Json(name = "pagesCount")
    override val pagesCount: Int,
    @Json(name = "films")
    override val films: List<ru.kanogor.skillcinema.data.retrofit.dto.TopFilmsList>?
) : TopFilms

@JsonClass(generateAdapter = true)
data class TopFilmsList(
    @Json(name = "countries")
    override val countries: List<ru.kanogor.skillcinema.data.retrofit.dto.Country>,
    @Json(name = "filmLength")
    override val filmLength: String?,
    @Json(name = "genres")
    override val genres: List<ru.kanogor.skillcinema.data.retrofit.dto.Genre>,
    @Json(name = "filmId")
    override val filmId: Int,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "nameRu")
    override val nameRu: String,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String,
    @Json(name = "premiereRu")
    override val premiereRu: String?,
    @Json(name = "year")
    override val year: String,
    @Json(name = "rating")
    override val rating: String?,
    @Json(name = "ratingVoteCount")
    override val ratingVoteCount: Int
) : TopFilmsList

@JsonClass(generateAdapter = true)
data class Country(
    @Json(name = "country")
    override val country: String
) : Country

@JsonClass(generateAdapter = true)
data class Genre(
    @Json(name = "genre")
    override val genre: String
) : Genre