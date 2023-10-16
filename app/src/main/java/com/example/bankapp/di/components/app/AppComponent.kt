package com.example.bankapp.di.components.app

import android.app.Application
import com.example.bankapp.App
import com.example.bankapp.di.modules.ContextModule
import com.example.bankapp.di.modules.DatabaseModule
import com.example.bankapp.di.modules.MainActivityModule
import com.example.bankapp.di.modules.RepositoryModule
import com.example.bankapp.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        MainActivityModule::class,
        ViewModelModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        ContextModule::class
    ]
)

interface AppComponent {

    fun inject(application: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}