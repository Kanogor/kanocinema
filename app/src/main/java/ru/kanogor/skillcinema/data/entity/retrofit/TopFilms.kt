package ru.kanogor.skillcinema.data.entity.retrofit

interface TopFilms {
    val pagesCount: Int
    val films: List<TopFilmsList>?
}

interface TopFilmsList {
    val countries: List<Country>
    val filmLength: String?
    val genres: List<Genre>
    val filmId: Int
    val nameEn: String?
    val nameRu: String
    val posterUrl: String
    val posterUrlPreview: String
    val premiereRu: String?
    val year: String
    val rating: String?
    val ratingVoteCount: Int
}

interface Country {
    val country: String
}

interface Genre {
    val genre: String
}


