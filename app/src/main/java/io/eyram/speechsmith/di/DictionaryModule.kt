package io.eyram.speechsmith.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.eyram.speechsmith.network.DictionaryService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryModule {

    @Provides
    @Singleton
    fun provideDictionaryService(): DictionaryService {
        return Retrofit.Builder()
            .baseUrl("https://api.chucknorris.io/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(DictionaryService::class.java)
    }
}