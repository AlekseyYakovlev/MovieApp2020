package ru.spb.yakovlev.movieapp2020.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import ru.spb.yakovlev.movieapp2020.data.remote.NetworkMonitor
import ru.spb.yakovlev.movieapp2020.data.remote.err.NoNetworkError
import timber.log.Timber

class NetworkStatusInterceptor(private val monitor: NetworkMonitor) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!monitor.isConnected) {
            Timber.e("NetworkStatusInterceptor: Network is not available")
            throw NoNetworkError()
        }
        return chain.proceed(chain.request())
    }
}