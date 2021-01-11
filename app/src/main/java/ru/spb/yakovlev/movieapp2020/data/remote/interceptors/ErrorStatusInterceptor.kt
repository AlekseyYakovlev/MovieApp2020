package ru.spb.yakovlev.movieapp2020.data.remote.interceptors

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import ru.spb.yakovlev.movieapp2020.data.remote.err.ApiError
import ru.spb.yakovlev.movieapp2020.data.remote.err.ErrorBody
import timber.log.Timber
import javax.inject.Inject

class ErrorStatusInterceptor @Inject constructor(private val json: Json) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (response.isSuccessful) return response

        val errMessage = try {
            response.body?.let { json.decodeFromString<ErrorBody>(it.string()) }?.status_message
        } catch (e: SerializationException) {
            Timber.e(e)
            e.message
        }

        when (response.code) {
            400 -> throw ApiError.BadRequest(errMessage)
            401 -> throw ApiError.Unauthorized(errMessage)
            403 -> throw ApiError.Forbidden(errMessage)
            404 -> throw ApiError.NotFound(errMessage)
            500 -> throw ApiError.InternalServerError(errMessage)
            else -> throw ApiError.UnknownError(errMessage)
        }
    }
}