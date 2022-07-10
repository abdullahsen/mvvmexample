package com.asen.mvvmexample.di

import android.app.Application
import androidx.room.Room
import com.asen.mvvmexample.BuildConfig
import com.asen.mvvmexample.R
import com.asen.mvvmexample.data.local.AppDatabase
import com.asen.mvvmexample.data.remote.AuthInterceptor
import com.asen.mvvmexample.data.remote.api.MovieService
import com.asen.mvvmexample.data.repository.MovieRepository
import com.asen.mvvmexample.data.repository.MovieRepositoryImpl
import com.asen.mvvmexample.data.repository.mapper.MovieEntityToMovieModelMapper
import com.asen.mvvmexample.data.repository.mapper.MovieResponseToMovieEntityMapper
import com.asen.mvvmexample.ui.viewmodel.details.MovieDetailsViewModel
import com.asen.mvvmexample.ui.viewmodel.overview.MoviesOverviewViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val appModules = module {
    factory { AuthInterceptor() }
    factory { provideOkHttpClient(get()) }
    factory { provideMovieApi(get()) }
    single { provideRetrofit(get()) }
    single { provideDatabase(androidApplication()) }
    single { provideMovieDao(get()) }
    factory { MovieEntityToMovieModelMapper() }
    factory { MovieResponseToMovieEntityMapper() }
    single<MovieRepository> { MovieRepositoryImpl(get(), get(), get(), get()) }
    viewModel { MoviesOverviewViewModel(get()) }
    viewModel { MovieDetailsViewModel(get()) }
    single { provideRequestOptions() }
    single { provideRequestManager(androidApplication(), get()) }
}

val contentType: MediaType = MediaType.get("application/json")

private val json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    isLenient = true
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder().baseUrl(BuildConfig.API_URL).client(okHttpClient)
        .addConverterFactory(
            json.asConverterFactory(contentType)
        ).build()
}

private fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
    return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
}

private fun provideMovieApi(retrofit: Retrofit): MovieService =
    retrofit.create(MovieService::class.java)

private fun provideDatabase(application: Application): AppDatabase =
    Room.databaseBuilder(application, AppDatabase::class.java, "movie_database")
        .fallbackToDestructiveMigration()
        .build()

private fun provideMovieDao(database: AppDatabase) = database.movieDao()

private fun provideRequestManager(
    application: Application,
    requestOptions: RequestOptions
): RequestManager {
    return Glide.with(application)
        .setDefaultRequestOptions(requestOptions)
}

private fun provideRequestOptions(): RequestOptions {
    return RequestOptions()
        .placeholder(R.drawable.ic_launcher_foreground)
        .error(R.drawable.ic_launcher_foreground)
}