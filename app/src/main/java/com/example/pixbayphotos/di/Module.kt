package com.example.pixbayphotos.di

import android.content.Context
import androidx.room.Room
import com.example.pixbayphotos.BuildConfig.BASE_URL
import com.example.pixbayphotos.BuildConfig.KEY
import com.example.pixbayphotos.network.ApiService
import com.example.pixbayphotos.db.AppDatabase
import com.example.pixbayphotos.db.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named(Constants.BASE_URL_CONSTANT)
    fun provideBaseUrl(): String = BASE_URL

    @Provides
    @Singleton
    @Named(Constants.KEY_CONSTANT)
    fun provideKey(): String = KEY


    @Provides
    @Singleton
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO


    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        @Named(Constants.BASE_URL_CONSTANT) baseUrl: String
    ): Retrofit =
        Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideImageDao(appDatabase: AppDatabase): ImageDao {
        return appDatabase.imagesDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "ImageFinderAppDb"
        ).build()
    }


}