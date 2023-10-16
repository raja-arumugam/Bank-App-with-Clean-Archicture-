package com.example.bankapp.di.modules

import com.example.bankapp.presentation.fragments.TransactionListFragment
import com.example.bankapp.presentation.fragments.DepositFragment
import com.example.bankapp.presentation.fragments.PayUserFragment
import com.example.bankapp.presentation.fragments.HomeFragment
import com.example.bankapp.presentation.fragments.LoginFragment
import com.example.bankapp.presentation.fragments.ProfileFragment
import com.example.bankapp.presentation.fragments.RegistrationFragment
import com.example.bankapp.presentation.fragments.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashFragment(): SplashFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeUserDetailFragmentFragment(): PayUserFragment

    @ContributesAndroidInjector
    abstract fun contributeDepositFragment(): DepositFragment

    @ContributesAndroidInjector
    abstract fun contributeTransactionListFragment(): TransactionListFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment
}