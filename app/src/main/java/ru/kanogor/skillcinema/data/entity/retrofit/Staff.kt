package ru.kanogor.skillcinema.data.entity.retrofit

interface Staff {
    val staffId: Int
    val nameRu: String
    val nameEn: String
    val description: String?
    val posterUrl: String
    val professionText: String
    val professionKey: String
}
