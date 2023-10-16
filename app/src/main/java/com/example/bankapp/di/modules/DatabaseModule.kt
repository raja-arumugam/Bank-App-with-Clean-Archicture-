package com.example.bankapp.di.modules

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.bankapp.data.local.db.BankDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @RequiresApi(Build.VERSION_CODES.O)
    @Singleton
    @Provides
    fun provideRegistrationsDb(app: Application) = BankDataBase.getInstance(app)

    @Singleton
    @Provides
    fun provideAuthDao(db: BankDataBase) = db.authDao()
}