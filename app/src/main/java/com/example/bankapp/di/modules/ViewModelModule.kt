package com.example.bankapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bankapp.di.components.viewmodel.ViewModelFactory
import com.example.bankapp.di.components.viewmodel.ViewModelKey
import com.example.bankapp.presentation.viewmodel.DepositViewModel
import com.example.bankapp.presentation.viewmodel.HomeViewModel
import com.example.bankapp.presentation.viewmodel.LoginViewModel
import com.example.bankapp.presentation.viewmodel.PayUserViewModel
import com.example.bankapp.presentation.viewmodel.ProfileViewModel
import com.example.bankapp.presentation.viewmodel.RegistrationViewModel
import com.example.bankapp.presentation.viewmodel.SplashViewModel
import com.example.bankapp.presentation.viewmodel.TransactionListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    abstract fun bindRegistrationViewModel(viewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PayUserViewModel::class)
    abstract fun bindPayUserViewModel(viewModel: PayUserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DepositViewModel::class)
    abstract fun bindDepositViewModel(viewModel: DepositViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionListViewModel::class)
    abstract fun bindTransactionListViewModel(viewModel: TransactionListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}