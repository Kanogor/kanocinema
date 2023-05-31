package ru.kanogor.skillcinema.data.retrofit.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.skillcinema.data.entity.retrofit.PremieresFilms
import ru.kanogor.skillcinema.data.entity.retrofit.PremieresFilmsList

@JsonClass(generateAdapter = true)
data class PremieresFilmDto(
    @Json(name = "total")
    override val total: Int,
    @Json(name = "items")
    override val items: List<ru.kanogor.skillcinema.data.retrofit.dto.PremieresFilmsList>?
) : PremieresFilms

@JsonClass(generateAdapter = true)
data class PremieresFilmsList(
    @Json(name = "countries")
    override val countries: List<Country>,
    @Json(name = "duration")
    override val duration: Int?,
    @Json(name = "genres")
    override val genres: List<Genre>,
    @Json(name = "kinopoiskId")
    override val kinopoiskId: Int,
    @Json(name = "nameEn")
    override val nameEn: String,
    @Json(name = "nameRu")
    override val nameRu: String,
    @Json(name = "posterUrl")
    override val posterUrl: String,
    @Json(name = "posterUrlPreview")
    override val posterUrlPreview: String,
    @Json(name = "premiereRu")
    override val premiereRu: String,
    @Json(name = "year")
    override val year: Int?
) : PremieresFilmsList
