package ru.kanogor.skillcinema.data.entity.retrofit

interface PersonInfo {
    val personId: Int
    val nameRu: String?
    val nameEn: String?
    val sex: String?
    val posterUrl: String?
    val profession: String?
    val films: List<StaffFilms>
}

interface StaffFilms {
    val filmId: Int
    val description: String
    val professionKey: String
    val nameRu: String?
    val nameEn: String?
    val rating: String?
    val general: Boolean

}
