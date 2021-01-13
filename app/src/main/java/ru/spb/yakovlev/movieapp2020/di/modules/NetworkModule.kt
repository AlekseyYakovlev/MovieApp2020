package ru.spb.yakovlev.movieapp2020.di.modules

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.spb.yakovlev.movieapp2020.BuildConfig
import ru.spb.yakovlev.movieapp2020.data.remote.NetworkMonitor
import ru.spb.yakovlev.movieapp2020.data.remote.RestService
import ru.spb.yakovlev.movieapp2020.data.remote.interceptors.ApiKeyAuthenticator
import ru.spb.yakovlev.movieapp2020.data.remote.interceptors.ErrorStatusInterceptor
import ru.spb.yakovlev.movieapp2020.data.remote.interceptors.NetworkStatusInterceptor
import ru.spb.yakovlev.movieapp2020.model.ApiKey
import ru.spb.yakovlev.movieapp2020.model.ApiSettings
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideRestService(
        retrofit: Retrofit
    ): RestService =
        retrofit.create(RestService::class.java)

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        json: Json
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        //tokenAuthenticator: TokenAuthenticator,
        apiKeyAuthenticator: ApiKeyAuthenticator,
        networkStatusInterceptor: NetworkStatusInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        errorStatusInterceptor: ErrorStatusInterceptor
    ): OkHttpClient =
        OkHttpClient().newBuilder()
            .readTimeout(2, TimeUnit.SECONDS)  // socket timeout (GET)
            .writeTimeout(5, TimeUnit.SECONDS) // socket timeout (POST, PUT, etc.)
            .addInterceptor(networkStatusInterceptor)  // intercept network status
            .addInterceptor(apiKeyAuthenticator)
            // .authenticator(tokenAuthenticator)         // refresh token if response code == 401
            .addInterceptor(httpLoggingInterceptor)    // log requests/results
            .addInterceptor(errorStatusInterceptor)    // intercept network errors
            .build()

    @Provides
    @Singleton
    fun provideApiKeyAuthenticator(
        apiKey: ApiKey
    ): ApiKeyAuthenticator =
        ApiKeyAuthenticator(apiKey.value)

    @Provides
    @Singleton
    fun provideNetworkStatusInterceptor(
        monitor: NetworkMonitor
    ): NetworkStatusInterceptor =
        NetworkStatusInterceptor(monitor)

    @Provides
    @Singleton
    fun provideErrorStatusInterceptor(
        json: Json
    ): ErrorStatusInterceptor =
        ErrorStatusInterceptor(json)

//TODO Add Authentication via token
//    @Provides
//    @Singleton
//    fun provideTokenAuthenticator(
//        prefs: PrefManager,
//        lazyApi: Lazy<RestService>
//    ): TokenAuthenticator =
//        TokenAuthenticator(prefs, lazyApi)

    @Provides
    @Singleton
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    fun provideApiKey(): ApiKey = ApiKey(
        BuildConfig.API_KEY
    )

    @Provides
    @Singleton
    fun provideApiSettings(): ApiSettings =
        ApiSettings()
}