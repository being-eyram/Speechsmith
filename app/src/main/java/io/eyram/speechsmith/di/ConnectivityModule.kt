package io.eyram.speechsmith.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ConnectivityModule {

    @Provides
    @Singleton
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        getSystemService(context, ConnectivityManager::class.java)!!
}