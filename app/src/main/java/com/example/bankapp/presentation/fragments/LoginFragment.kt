package com.example.bankapp.presentation.fragments

import android.os.Build
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.bankapp.R
import com.example.bankapp.common.SingleToast
import com.example.bankapp.databinding.FragmentLoginBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.state.LoginState
import com.example.bankapp.presentation.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LoginFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginBinding: FragmentLoginBinding
    private lateinit var mViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        loginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        loginBinding.lifecycleOwner = viewLifecycleOwner

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window? = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.medium_orange)
        }

        with(loginBinding) {
            btLogin.setOnClickListener {
                lifecycleScope.launch(Dispatchers.IO) {
                    loginUser()
                }
            }

            navRegister.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.loginFragment) findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                )
            }
        }
        return loginBinding.root
    }

    private suspend fun loginUser() {
        val email = loginBinding.etEmail.text.toString()
        val password = loginBinding.etPassword.text.toString()

        mViewModel.checkInputValidation(email, password)
        getValidationObserver()
    }

    private suspend fun getValidationObserver() {
        mViewModel.validateLoginState.collectLatest { uiState ->
            if (!uiState.isInputValid) {
                withContext(Dispatchers.Main) {
                    SingleToast.show(
                        requireActivity(),
                        uiState.errorMessageInput.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            } else {
                mViewModel.login()
                getLoginObserver()
            }
        }
    }

    private suspend fun getLoginObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.loginState.collectLatest {
                when (it) {
                    is LoginState.Error -> {
                        SingleToast.show(
                            requireActivity(),
                            resources.getString(R.string.invalid_user_password),
                            Toast.LENGTH_SHORT
                        )
                    }

                    is LoginState.Success -> {
                        if (findNavController().currentDestination?.id == R.id.loginFragment)
                            findNavController().navigate(
                            LoginFragmentDirections.actionLoginFragmentToHomeFragment2(
                                it.data.email
                            )
                        )

                        mViewModel.saveLoggedIn(isLoggedIn = true)
                        mViewModel.saveLoggedInEmail(it.data.email)

                    }

                    else -> Unit
                }
            }
        }
    }


}