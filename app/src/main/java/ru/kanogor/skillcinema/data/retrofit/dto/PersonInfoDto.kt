package ru.kanogor.skillcinema.data.retrofit.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.kanogor.skillcinema.data.entity.retrofit.PersonInfo
import ru.kanogor.skillcinema.data.entity.retrofit.StaffFilms

@JsonClass(generateAdapter = true)
data class PersonInfoDto(
    @Json(name = "personId")
    override val personId: Int,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "posterUrl")
    override val posterUrl: String?,
    @Json(name = "profession")
    override val profession: String?,
    @Json(name = "films")
    override val films: List<ru.kanogor.skillcinema.data.retrofit.dto.StaffFilms>,
    @Json(name = "sex")
    override val sex: String?
) : PersonInfo

@JsonClass(generateAdapter = true)
data class StaffFilms(
    @Json(name = "filmId")
    override val filmId: Int,
    @Json(name = "description")
    override val description: String,
    @Json(name = "professionKey")
    override val professionKey: String,
    @Json(name = "nameRu")
    override val nameRu: String?,
    @Json(name = "nameEn")
    override val nameEn: String?,
    @Json(name = "rating")
    override val rating: String?,
    @Json(name = "general")
    override val general: Boolean
) : StaffFilms