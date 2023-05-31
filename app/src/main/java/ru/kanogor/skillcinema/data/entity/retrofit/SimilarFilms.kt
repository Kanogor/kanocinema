package ru.kanogor.skillcinema.data.entity.retrofit

interface SimilarFilms {
    val total: Int
    val items: List<SimilarFilm>
}

interface SimilarFilm {
    val filmId: Int
}
