package ru.kanogor.skillcinema.data.entity.retrofit

interface FilteredFilms {
    val total: Int
    val totalPages: Int
    val items: List<FilteredFilmsList>?
}

interface FilteredFilmsList {
    val kinopoiskId: Int
    val imdbId: String?
    val nameRu: String?
    val nameEn: String?
    val nameOriginal: String?
    val countries: List<Country>
    val genres: List<Genre>
    val ratingKinopoisk: Double?
    val ratingImdb: Double?
    val year: Int?
    val type: String
    val posterUrl: String
    val posterUrlPreview: String
}
