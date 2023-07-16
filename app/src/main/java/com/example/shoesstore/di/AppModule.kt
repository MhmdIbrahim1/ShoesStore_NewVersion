package com.example.shoesstore.di

import android.app.Application
import android.content.Context
import com.example.shoesstore.util.Constants.LOGIN_KEY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStoreDatabase() = Firebase.firestore


    @Provides
    @Singleton
    fun provideIntroductionSP(
        application: Application
    ) = application.getSharedPreferences(LOGIN_KEY, Context.MODE_PRIVATE)
}