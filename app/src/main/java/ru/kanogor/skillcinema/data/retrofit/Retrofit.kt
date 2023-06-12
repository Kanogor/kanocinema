package ru.kanogor.skillcinema.data.retrofit

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kanogor.skillcinema.data.retrofit.dto.FilteredFilmsDto
import ru.kanogor.skillcinema.data.retrofit.dto.GalleryItemsDto
import ru.kanogor.skillcinema.data.retrofit.dto.IdFilmDto
import ru.kanogor.skillcinema.data.retrofit.dto.PersonInfoDto
import ru.kanogor.skillcinema.data.retrofit.dto.PremieresFilmDto
import ru.kanogor.skillcinema.data.retrofit.dto.SeasonsInfoDto
import ru.kanogor.skillcinema.data.retrofit.dto.SimilarFilmsDto
import ru.kanogor.skillcinema.data.retrofit.dto.StaffDto
import ru.kanogor.skillcinema.data.retrofit.dto.TopFilmsDto
import javax.inject.Singleton

private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideSearchApiInfo(retrofit: Retrofit): SearchApiInfo =
        retrofit.create(SearchApiInfo::class.java)

    @Singleton
    @Provides
    fun provideRepository(searchApiInfo: SearchApiInfo) = FilmRepository(searchApiInfo)

}

interface SearchApiInfo {

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films/top")
    suspend fun getTopFilms(
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<TopFilmsDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films/premieres")
    suspend fun getPremieres(
        @Query("year") year: Int,
        @Query("month") month: String
    ): Response<PremieresFilmDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films/{id}")
    suspend fun getIdFilm(
        @Path("id") id: Int
    ): Response<IdFilmDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films")
    suspend fun getSoaps(
        @Query("type") type: String = "TV_SERIES",
        @Query("ratingFrom") ratingFrom: Int = 8,
        @Query("page") page: Int
    ): Response<FilteredFilmsDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films")
    suspend fun getRandomFilms(
        @Query("countries") countries: List<Int>,
        @Query("genres") genres: List<Int>,
        @Query("type") type: String = "FILM",
        @Query("ratingFrom") ratingFrom: Int = 8,
        @Query("page") page: Int
    ): Response<FilteredFilmsDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v1/staff")
    suspend fun getStaff(
        @Query("filmId") filmId: Int
    ): Response<List<StaffDto>>

    @Headers("X-API-KEY: $api_key")
    @GET("v1/staff/{id}")
    suspend fun getPersonInfo(
        @Path("id") id: Int
    ): Response<PersonInfoDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films/{id}/images")
    suspend fun getPictures(
        @Path("id") id: Int,
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<GalleryItemsDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films/{id}/similars")
    suspend fun getSimilarFilms(
        @Path("id") id: Int
    ): Response<SimilarFilmsDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films/{id}/seasons")
    suspend fun getSeasonsInfo(
        @Path("id") id: Int
    ): Response<SeasonsInfoDto>

    @Headers("X-API-KEY: $api_key")
    @GET("v2.2/films")
    suspend fun getSearch(
        @Query("countries") countries: List<Int>,
        @Query("genres") genres: List<Int>,
        @Query("type") type: String,
        @Query("order") order: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): Response<FilteredFilmsDto>

    private companion object {
        private const val api_key = "44b21c34-f124-45de-b23e-b5c3e6f0e62d"
        //api 06258519-3105-4f81-bfe0-422c4ddb4716 / 44b21c34-f124-45de-b23e-b5c3e6f0e62d / ce6f81de-e746-4a8b-8a79-4a7fe451b75d / 1ba8d04f-2102-4a63-b7af-77beedf1d615
    }
}
