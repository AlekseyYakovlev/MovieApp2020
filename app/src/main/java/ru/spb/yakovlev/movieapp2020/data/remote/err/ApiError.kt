package ru.spb.yakovlev.movieapp2020.data.remote.err

import kotlinx.serialization.Serializable
import java.io.IOException

sealed class ApiError(override val message: String) : IOException(message) {
    class BadRequest(message: String?) : ApiError(message ?: "Bad Request")
    class Unauthorized(message: String?) : ApiError(message ?: "Authorization token required")
    class Forbidden(message: String?) : ApiError(message ?: "Access denied")
    class NotFound(message: String?) : ApiError(message ?: "Requested page not found")
    class InternalServerError(message: String?) : ApiError(message ?: "Internal server error")
    class UnknownError(message: String?) : ApiError(message ?: "Unknown error")
}

@Serializable
class ErrorBody(val status_message: String)