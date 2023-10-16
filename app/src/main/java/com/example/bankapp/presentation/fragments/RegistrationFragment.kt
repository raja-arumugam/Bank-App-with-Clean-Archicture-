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
import com.example.bankapp.databinding.FragmentRegistrationBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.viewmodel.RegistrationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegistrationFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var registrationBinding: FragmentRegistrationBinding
    private lateinit var mViewModel: RegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registrationBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        registrationBinding.lifecycleOwner = viewLifecycleOwner

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window? = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.medium_orange)
        }

        registrationBinding.btRegister.setOnClickListener {
            lifecycleScope.launch {
                checkValidation()
            }
        }

        registrationBinding.navLogin.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.registrationFragment) findNavController().navigate(
                RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            )
        }

        return registrationBinding.root
    }

    private suspend fun checkValidation() {
        val name = registrationBinding.etName.text.toString()
        val email = registrationBinding.etEmail.text.toString()
        val password = registrationBinding.etPassword.text.toString()
        val confirmPassword = registrationBinding.etConfirmPassword.text.toString()
        val bankName = registrationBinding.etBankName.text.toString()
        val ifscNumber = registrationBinding.etIfsc.text.toString()
        val accountNumber = registrationBinding.etAcNum.text.toString()
        val amount = registrationBinding.etAmount.text.toString()

        mViewModel.checkInputValidation(
            name,
            email,
            password,
            confirmPassword,
            bankName,
            ifscNumber,
            accountNumber,
            amount
        )

        getValidationObserver()
    }

    private suspend fun getValidationObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.validateRegisterState.collectLatest { uiState ->
                if (!uiState.isInputValid) {
                    SingleToast.show(
                        requireActivity(),
                        uiState.errorMessageInput.toString(),
                        Toast.LENGTH_SHORT
                    )
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        mViewModel.checkUserAlreadyExist()

                        getUserExistObserver()
                    }
                }
            }
        }
    }

    private suspend fun getUserExistObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.userExist.collectLatest {
                if (it.exist != null) {
                    if (it.exist == true) {
                        SingleToast.show(
                            requireActivity(),
                            resources.getString(R.string.user_exist),
                            Toast.LENGTH_SHORT
                        )
                    } else {
                        mViewModel.userRegistration()
                        getRegistrationObserver()
                    }
                }
            }
        }
    }

    private suspend fun getRegistrationObserver() {
        mViewModel.registrationState.collectLatest { registerResult ->
            withContext(Dispatchers.Main) {
                if (registerResult.userRegistration == 0) {

                    SingleToast.show(
                        requireActivity(),
                        resources.getString(R.string.register_done),
                        Toast.LENGTH_SHORT
                    )

                    if (findNavController().currentDestination?.id == R.id.registrationFragment) findNavController().navigate(
                        RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment2(
                            mViewModel.validateRegisterState.value.emailInput
                        )
                    )

                    mViewModel.saveLoggedIn(isLoggedIn = true)
                    mViewModel.saveLoggedInEmail(mViewModel.validateRegisterState.value.emailInput)
                }

                if (registerResult.error != null) {
                    SingleToast.show(
                        requireActivity(),
                        registerResult.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }

}