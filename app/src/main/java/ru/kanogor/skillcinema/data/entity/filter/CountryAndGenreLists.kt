package ru.kanogor.skillcinema.data.entity.filter

class CountryAndGenreLists {

    val countryList: List<List<Int>> = listOf(
        listOf(1),  // 1 - CША
        listOf(3),  //  3 - Франция
        listOf(5),  //  5 - Великобритания
        listOf(34), //  34 - Россия
    )

    val genreList: List<List<Int>> = listOf(
        listOf(2),   // 2 - драма
        listOf(4),   // 4 - мелодрама
        listOf(5),   // 5 - детектив
        listOf(11),  // 11 - боевик
        listOf(12),  // 12 - фентези
        listOf(13),  // 13 - комедия
    )
}