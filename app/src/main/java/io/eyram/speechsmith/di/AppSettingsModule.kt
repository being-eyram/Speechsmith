package io.eyram.speechsmith.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.eyram.speechsmith.data.preferences.AppSettings
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppSettingsModule {

    @Singleton
    @Provides
    fun provideAppSettings(@ApplicationContext context: Context): AppSettings =
        AppSettings(context)
}