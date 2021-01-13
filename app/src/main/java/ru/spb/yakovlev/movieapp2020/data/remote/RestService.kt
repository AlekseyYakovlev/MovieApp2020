package ru.spb.yakovlev.movieapp2020.data.remote

import ru.spb.yakovlev.movieapp2020.data.remote.resp.GenresResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.spb.yakovlev.movieapp2020.data.remote.resp.*

interface RestService {

    @GET("configuration")
    suspend fun getConfiguration(): ConfigurationResponse

   @GET("movie/now_playing")
   suspend fun getNowPlaying(
       @Query("language") language:  String = "en-US",
       @Query("page") page:  Int = 1,
       @Query("region") region:  String? = null,
   ): MoviesListResponse

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("language") language:  String = "en-US",
        @Query("page") page:  Int = 1,
        @Query("region") region:  String? = null,
    ): MoviesListResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("language") language:  String = "en-US",
        @Query("page") page:  Int = 1,
        @Query("region") region:  String? = null,
    ): MoviesListResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("language") language:  String = "en-US",
        @Query("page") page:  Int = 1,
        @Query("region") region:  String? = null,
    ): MoviesListResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query:  String,
        @Query("include_adult") includeAdult:  Boolean = false,
        @Query("year") year:  Int? = null,
        @Query("primary_release_year") primaryReleaseYear:  Int? = null,
        @Query("language") language:  String = "en-US",
        @Query("page") page:  Int = 1,
        @Query("region") region:  String? = null,
    ): MoviesListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language:  String = "en-US",
    ): MovieDetailsResponse

    @GET("person/{person_id}")
    suspend fun getPerson(
        @Path("person_id") personId: Int,
        @Query("language") language:  String = "en-US",
    ): PersonResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("language") language:  String = "en-US",
    ): CreditsResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") language:  String = "en-US",
    ): GenresResponse

    @GET("movie/{movie_id}/release_dates")
    suspend fun getMovieReleaseDates(
        @Path("movie_id") movieId: Int,
    ): MovieReleaseDatesResponse

}