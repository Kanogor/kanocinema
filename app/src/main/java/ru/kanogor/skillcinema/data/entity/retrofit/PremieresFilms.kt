package ru.kanogor.skillcinema.data.entity.retrofit

interface PremieresFilms {
    val total: Int
    val items: List<PremieresFilmsList>?
}

interface PremieresFilmsList {
    val countries: List<Country>
    val duration: Int?
    val genres: List<Genre>
    val kinopoiskId: Int
    val nameEn: String
    val nameRu: String
    val posterUrl: String
    val posterUrlPreview: String
    val premiereRu: String
    val year: Int?
}


