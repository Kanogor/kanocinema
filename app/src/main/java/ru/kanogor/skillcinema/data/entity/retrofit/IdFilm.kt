package ru.kanogor.skillcinema.data.entity.retrofit

interface IdFilm {
    val kinopoiskId: Int
    val imdbId: String?
    val nameRu: String?
    val posterUrl: String
    val posterUrlPreview: String
    val ratingKinopoisk: Double?
    val countries: List<Country>
    val genres: List<Genre>
    val description: String?
    val year: String?
    val filmLength: Int?
    val ratingAwait: Double?
    val ratingImdb: Double?
    val nameEn: String?
    val nameOriginal: String?
    val coverUrl: String?
    val logoUrl: String?
    val ratingAgeLimits: String?
    val shortDescription: String?
    val serial: Boolean
}