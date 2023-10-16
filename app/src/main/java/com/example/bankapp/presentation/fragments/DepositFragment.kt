package com.example.bankapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bankapp.R
import com.example.bankapp.common.SingleToast
import com.example.bankapp.databinding.FragmentDepositBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.viewmodel.DepositViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DepositFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentDepositBinding
    private lateinit var mViewModel: DepositViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_deposit, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.toolbarTitle.text = resources.getString(R.string.deposit)

        binding.btLogin.setOnClickListener {
            val amount = binding.etPayAmount.text.toString()

            lifecycleScope.launch {
                if (amount != "") {
                    mViewModel.checkInputValidation(amount)
                    getValidationObserver()
                } else {
                    withContext(Dispatchers.Main) {
                        SingleToast.show(
                            requireActivity(),
                            resources.getString(R.string.please_enter_amount),
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            mViewModel.getLoggedInEmail()
            mViewModel.loggedInEmail.collectLatest { email ->
                if (email != "") {
                    mViewModel.getUserData(email)
                    getUserDetailObserver()
                }
            }
        }
    }

    private suspend fun getValidationObserver() {
        mViewModel.validateAmountState.collectLatest { uiState ->
            if (!uiState.isInputValid) {
                withContext(Dispatchers.Main) {
                    SingleToast.show(
                        requireActivity(),
                        uiState.errorMessageInput.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            } else {
                mViewModel.depositAmount()
                getDepositObserver()
            }
        }
    }

    // get logged-in user details
    private suspend fun getUserDetailObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.getUser.collectLatest { result ->

                if (result.error != null) {
                    binding.pbDeposit.visibility = View.GONE
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                if (result.isLoading) {
                    binding.pbDeposit.visibility = View.VISIBLE
                }

                result.userData?.collectLatest { user ->
                    with(binding) {
                        pbDeposit.visibility = View.GONE

                        binding.llCard.tvName.text = user.name
                        binding.llCard.tvAccountNumber.text = user.accountNumber
                        binding.llCard.tvAmount.text =
                            resources.getString(R.string.rs) + " " + user.amount
                        mViewModel.currentAmount.value = user.amount.toInt()
                    }
                }
            }
        }
    }

    private suspend fun getDepositObserver() {
        mViewModel.amountDepositState.collectLatest {
            if (it.deposit != null) {
                if (it.deposit == false) {
                    SingleToast.show(
                        requireActivity(),
                        resources.getString(R.string.unable_to_add_amount),
                        Toast.LENGTH_SHORT
                    )
                } else {
                    SingleToast.show(
                        requireActivity(),
                        resources.getString(R.string.amount_added_success),
                        Toast.LENGTH_SHORT
                    )
                    binding.etPayAmount.text?.clear()
                }
            }
        }
    }
}
