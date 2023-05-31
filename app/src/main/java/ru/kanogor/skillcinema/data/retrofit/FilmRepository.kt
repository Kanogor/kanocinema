package ru.kanogor.skillcinema.data.retrofit

import ru.kanogor.skillcinema.data.retrofit.dto.FilteredFilmsList
import ru.kanogor.skillcinema.data.retrofit.dto.GalleryItemsDto
import ru.kanogor.skillcinema.data.retrofit.dto.IdFilmDto
import ru.kanogor.skillcinema.data.retrofit.dto.PersonInfoDto
import ru.kanogor.skillcinema.data.retrofit.dto.PremieresFilmsList
import ru.kanogor.skillcinema.data.retrofit.dto.SeasonsInfoDto
import ru.kanogor.skillcinema.data.retrofit.dto.SimilarFilmsDto
import ru.kanogor.skillcinema.data.retrofit.dto.StaffDto
import ru.kanogor.skillcinema.data.retrofit.dto.TopFilmsList
import ru.kanogor.skillcinema.data.entity.filter.CountryAndGenreLists
import ru.kanogor.skillcinema.data.entity.filter.PictureType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FilmRepository(private val searchApiInfo: SearchApiInfo) {

    val firstRandomCountry =
        CountryAndGenreLists().countryList[(CountryAndGenreLists().countryList.indices)
            .shuffled()
            .first()]

    val firstRandomGenre =
        CountryAndGenreLists().genreList[(CountryAndGenreLists().genreList.indices)
            .shuffled()
            .first()]

    val secondRandomCountry =
        CountryAndGenreLists().countryList[(CountryAndGenreLists().countryList.indices)
            .shuffled()
            .last()]

    val secondRandomGenre =
        CountryAndGenreLists().genreList[(CountryAndGenreLists().genreList.indices)
            .shuffled()
            .last()]

    fun getFirstRandomFilmsTitle(randomCountry: List<Int>, randomGenre: List<Int>): String {
        var country: String? = null
        var genre: String? = null
        when (randomCountry) {
            listOf(1) -> country = "CША"
            listOf(3) -> country = "Франции"
            listOf(5) -> country = "Великобритании"
            listOf(34) -> country = "России"
        }
        when (randomGenre) {
            listOf(2) -> genre = "Драмы"
            listOf(4) -> genre = "Мелодрамы"
            listOf(5) -> genre = "Детективы"
            listOf(11) -> genre = "Боевики"
            listOf(12) -> genre = "Фентези"
            listOf(13) -> genre = "Комедии"
        }
        return "$genre $country"
    }

    private val year: Int = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY")).toInt()
    private val monthNumber = LocalDate.now().format(DateTimeFormatter.ofPattern("MM"))
    private val month = when (monthNumber) {
        "01" -> "JANUARY"
        "02" -> "FEBRUARY"
        "03" -> "MARCH"
        "04" -> "APRIL"
        "05" -> "MAY"
        "06" -> "JUNE"
        "07" -> "JULY"
        "08" -> "AUGUST"
        "09" -> "SEPTEMBER"
        "10" -> "OCTOBER"
        "11" -> "NOVEMBER"
        "12" -> "DECEMBER"
        else -> "NONE"
    }

    private val pictureType = PictureType()

    suspend fun getPopularFilms(page: Int): List<TopFilmsList> {
        return searchApiInfo.getTopFilms("TOP_100_POPULAR_FILMS", page).body()!!.films!!
    }

    suspend fun getTopFilms(page: Int): List<TopFilmsList> {
        return searchApiInfo.getTopFilms("TOP_250_BEST_FILMS", page).body()!!.films!!
    }

    suspend fun getPremieresFilm(): List<PremieresFilmsList> {
        return searchApiInfo.getPremieres(year = year, month = month).body()!!.items!!
    }

    suspend fun getSoaps(page: Int): List<FilteredFilmsList> {
        return searchApiInfo.getSoaps(page = page).body()!!.items!!
    }

    suspend fun getIdFilm(id: Int): IdFilmDto {
        return searchApiInfo.getIdFilm(id).body()!!
    }

    suspend fun getSoapsCode(page: Int): Int {
        return searchApiInfo.getSoaps(page = page).code()
    }

    suspend fun getRandomFilms(
        randomCountry: List<Int>,
        randomGenre: List<Int>,
        page: Int
    ): List<FilteredFilmsList> {
        return searchApiInfo.getRandomFilms(
            countries = randomCountry,
            genres = randomGenre,
            page = page
        ).body()!!.items!!
    }

    suspend fun getStaff(filmId: Int): List<StaffDto> {
        return searchApiInfo.getStaff(filmId).body()!!
    }

    suspend fun getPersonInfo(personId: Int): PersonInfoDto {
        return searchApiInfo.getPersonInfo(personId).body()!!
    }

    suspend fun getPosterPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.poster, page = page).body()!!
    }

    suspend fun getStillPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.still, page = page).body()!!
    }

    suspend fun getShootingPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.shooting, page = page).body()!!
    }

    suspend fun getFanArtPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.fanArt, page = page).body()!!
    }

    suspend fun getPromoPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.promo, page = page).body()!!
    }

    suspend fun getConceptPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.concept, page = page).body()!!
    }

    suspend fun getWallPaperPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.wallpaper, page = page)
            .body()!!
    }

    suspend fun getCoverPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.cover, page = page).body()!!
    }

    suspend fun getScreenshotPictures(id: Int, page: Int): GalleryItemsDto {
        return searchApiInfo.getPictures(id = id, type = pictureType.screenshot, page = page)
            .body()!!
    }

    suspend fun getSimilarFilms(filmId: Int): SimilarFilmsDto {
        return searchApiInfo.getSimilarFilms(filmId).body()!!
    }

    suspend fun getSeasonsInfo(filmId: Int): SeasonsInfoDto {
        return searchApiInfo.getSeasonsInfo(filmId).body()!!
    }

    suspend fun getSearch(
        countries: List<Int>,
        genres: List<Int>,
        type: String,
        order: String,
        ratingFrom: Int,
        ratingTo: Int,
        yearFrom: Int,
        yearTo: Int,
        keyword: String,
        page: Int
    ): List<FilteredFilmsList> {
        return searchApiInfo.getSearch(
            countries,
            genres,
            type,
            order,
            ratingFrom,
            ratingTo,
            yearFrom,
            yearTo,
            keyword,
            page
        ).body()!!.items!!
    }


}
