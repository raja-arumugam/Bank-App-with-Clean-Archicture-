package com.example.bankapp.di.modules

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ContextModule {
    @Singleton
    @Binds
    abstract fun context(appInstance: Application): Context
}