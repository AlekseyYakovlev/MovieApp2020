package ru.spb.yakovlev.movieapp2020.data.remote.err

import java.io.IOException

class NoNetworkError(override val message: String = "Network is not available") : IOException(message)