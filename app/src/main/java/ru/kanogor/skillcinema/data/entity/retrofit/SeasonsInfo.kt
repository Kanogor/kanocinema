package ru.kanogor.skillcinema.data.entity.retrofit

interface SeasonsInfo {
    val total: Int
    val items: List<Season>
}

interface Season {
    val number: Int
    val episodes: List<Episode>
}

interface Episode {
    val seasonNumber: Int
    val episodeNumber: Int
    val nameRu: String?
    val nameEn: String?
    val releaseDate: String?
}
