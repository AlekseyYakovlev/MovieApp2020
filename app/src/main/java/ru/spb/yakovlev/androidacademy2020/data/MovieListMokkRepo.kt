package ru.spb.yakovlev.androidacademy2020.data

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.spb.yakovlev.androidacademy2020.App
import ru.spb.yakovlev.androidacademy2020.model.DataState
import ru.spb.yakovlev.androidacademy2020.model.MovieData

object MovieListMokkRepo {

    private val _moviesListState = MutableStateFlow<DataState<List<MovieData>>>(DataState.Empty)
    val moviesListState: StateFlow<DataState<List<MovieData>>> = _moviesListState
    private var moviesList: MutableList<MovieData> = mutableListOf()


    private val jsonFormat = Json { ignoreUnknownKeys = true }

    fun loadMovies() {
        GlobalScope.launch {
            (0..30).forEach {
                _moviesListState.value = DataState.Loading(it)
                delay(10L)
            }
            _moviesListState.value = DataState.Error("Loading error, restarting loading...")
            delay(500L)

            withContext(Dispatchers.IO) {
                coroutineScope {
                    launch {
                        (0..20).forEach {
                            _moviesListState.value = DataState.Loading(it)
                            delay(30L)
                        }
                    }
                    val genresMap = async {
                        val genresJson = readAssetFileToString("genres.json")
                        parseGenres(genresJson).associateBy { it.id }
                    }
                    val data = async { readAssetFileToString("data.json") }
                    moviesList = parseMovies(data.await(), genresMap.await()).toMutableList()

                    launch {
                        (20..100).forEach {
                            val index: Int = (it - 20) / 4
                            if (it % 4 == 0) _moviesListState.value =
                                DataState.Success(moviesList.take(index))
                            else _moviesListState.value = DataState.Loading(it)
                            delay(30L)
                        }
                    }
                }
            }
        }
    }


    private fun parseGenres(data: String): List<Genre> {
        val jsonGenres = jsonFormat.decodeFromString<List<JsonGenre>>(data)
        return jsonGenres.map { Genre(id = it.id, name = it.name) }
    }

    private fun readAssetFileToString(fileName: String): String {
        val stream = App.INSTANCE.assets.open(fileName)
        return stream.bufferedReader().readText()
    }

    private fun parseMovies(data: String, genresMap: Map<Int, Genre>): List<MovieData> {
        val jsonActors = jsonFormat.decodeFromString<List<JsonMovie>>(data)
        return jsonActors.map { jsonMovie ->
            MovieData(
                id = jsonMovie.id,
                title = jsonMovie.title,
                genres = jsonMovie.genreIds
                    .intersect(genresMap.keys)
                    .mapNotNull { genresMap[it]?.name }
                    .reduce { acc, genre -> "$acc, $genre" },
                runtime = jsonMovie.runtime,
                minimumAge = if (jsonMovie.adult) "16+" else "13+",
                rating = jsonMovie.ratings,
                numberOfRatings = jsonMovie.votesCount,
                poster = jsonMovie.posterPicture,
                poster2 = jsonMovie.backdropPicture,
                isLike = false,
                overview = jsonMovie.overview,
                castIds = jsonMovie.actors
            )
        }
    }

    fun getMovieById(moveId: Int): MovieData = moviesList.find { it.id == moveId } ?: moviesList[0]

    fun updateMovieLike(moveId: Int, isLike: Boolean) {
        _moviesListState.value = DataState.Loading(null)
        val oldCopy = getMovieById(moveId)
        val index = moviesList.indexOf(oldCopy)
        moviesList[index] = oldCopy.copy(isLike = isLike)
        _moviesListState.value = DataState.Success(moviesList)
    }

    @Serializable
    private class JsonMovie(
        val id: Int,
        val title: String,
        @SerialName("poster_path")
        val posterPicture: String,
        @SerialName("backdrop_path")
        val backdropPicture: String,
        val runtime: Int,
        @SerialName("genre_ids")
        val genreIds: List<Int>,
        val actors: List<Int>,
        @SerialName("vote_average")
        val ratings: Float,
        @SerialName("vote_count")
        val votesCount: Int,
        val overview: String,
        val adult: Boolean
    )

    @Serializable
    private class JsonGenre(val id: Int, val name: String)

}