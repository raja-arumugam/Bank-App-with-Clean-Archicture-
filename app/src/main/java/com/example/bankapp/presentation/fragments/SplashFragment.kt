package com.example.bankapp.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bankapp.R
import com.example.bankapp.databinding.FragmentSplashBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.viewmodel.LoginViewModel
import com.example.bankapp.presentation.viewmodel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mViewModel: SplashViewModel

    private lateinit var binding: FragmentSplashBinding
    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = viewLifecycleOwner

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window? = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.medium_orange)
        }

        activityScope.launch {
            delay(3000)

            if (mViewModel.userLoggedIn.value) {
                val homeDirections = SplashFragmentDirections.actionSplashFragmentToHomeFragment(
                    mViewModel.loggedInEmail.value)
                findNavController().navigate(homeDirections)
            } else {
                val splashFragmentDirection =
                    SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                findNavController().navigate(splashFragmentDirection)
            }
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        activityScope.cancel()
    }
}