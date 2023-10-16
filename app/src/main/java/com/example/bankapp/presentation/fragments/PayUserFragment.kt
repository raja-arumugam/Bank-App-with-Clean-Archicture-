package com.example.bankapp.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bankapp.R
import com.example.bankapp.common.Constants.REFERENCE_CODE_LENGTH
import com.example.bankapp.common.SingleToast
import com.example.bankapp.common.loadImage
import com.example.bankapp.databinding.FragmentPayUserBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.viewmodel.PayUserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PayUserFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var userDetailBinding: FragmentPayUserBinding
    private val navArgs: PayUserFragmentArgs by navArgs()
    private lateinit var mViewModel: PayUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pay_user, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        userDetailBinding.lifecycleOwner = viewLifecycleOwner

        val userID = navArgs.userID

        lifecycleScope.launch(Dispatchers.IO) {
            userDetailBinding.pbUserDetail.visibility = View.VISIBLE
            mViewModel.getUserData(userID)
            getUserDetailObserver()
        }

        with(userDetailBinding) {
            etPayAmount.requestFocus()
            etPayAmount.isFocusable = true
            etNotes.setHorizontallyScrolling(false)
            etNotes.maxLines = 5
            toolbarTitle.text = resources.getString(R.string.pay)
        }

        return userDetailBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            mViewModel.getLoggedInEmail()
            mViewModel.loggedInEmail.collectLatest { email ->
                if (email != "") {
                    mViewModel.getLoggedUserData(email)
                    getLoggedUserDetailObserver()
                }
            }
        }

        userDetailBinding.btShare.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val amount = userDetailBinding.etPayAmount.text.toString()
                val notes = userDetailBinding.etNotes.text.toString()

                if (amount != "" && notes != "") {
                    mViewModel.checkInputValidation(amount, notes)
                    getValidationObserver()
                } else {
                    withContext(Dispatchers.Main) {
                        SingleToast.show(
                            requireActivity(),
                            resources.getString(R.string.empty_fields_left),
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }
    }

    private suspend fun getValidationObserver() {
        mViewModel.validateTransactionState.collectLatest { uiState ->
            if (!uiState.isInputValid) {
                withContext(Dispatchers.Main) {
                    SingleToast.show(
                        requireActivity(),
                        uiState.errorMessageInput.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    val reference = (0..1000).random()
                    mViewModel.refNumber.value = reference
                    mViewModel.payAmount()
                    getPayAmountObserver()
                }
            }
        }
    }

    // Receiver(Selected) user details
    private suspend fun getUserDetailObserver() {
        mViewModel.selectedUser.collectLatest { result ->

            if (result.error != null) {
                userDetailBinding.pbUserDetail.visibility = View.GONE
                Toast.makeText(requireContext(), result.error.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

            if (result.isLoading) {
                userDetailBinding.pbUserDetail.visibility = View.VISIBLE
            }

            result.user?.collectLatest { user ->
                withContext(Dispatchers.Main) {
                    with(userDetailBinding) {
                        pbUserDetail.visibility = View.GONE

                        userDetailBinding.tvName.text = user.name
                        userDetailBinding.tvBank.text =
                            user.bankName + " (" + user.accountNumber + ")"

                        user.avatar.let {
                            ivPerson.loadImage(user.avatar)
                        }

                        mViewModel.receiver_ID.value = user.userId
                        mViewModel.receiver_Name.value = user.name
                        mViewModel.receiver_Bank.value = user.bankName
                        mViewModel.receiver_Image.value = user.avatar
                    }
                }
            }
        }
    }

    // LoggedIn User
    private suspend fun getLoggedUserDetailObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.getUser.collectLatest { result ->

                if (result.error != null) {
                    userDetailBinding.pbUserDetail.visibility = View.GONE
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                if (result.isLoading) {
                    userDetailBinding.pbUserDetail.visibility = View.VISIBLE
                }

                result.userData?.collectLatest { loggedInUser ->
                    mViewModel.sender_ID.value = loggedInUser.id
                    mViewModel.sender_Amount.value = loggedInUser.amount.toInt()
                }
            }
        }
    }

    // get amount transaction response
    private suspend fun getPayAmountObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.addTransactionState.collectLatest { result ->

                if (result.addTransactions == 0) {
                    SingleToast.show(
                        requireActivity(),
                        resources.getString(R.string.amount_added_success),
                        Toast.LENGTH_SHORT
                    )

                    userDetailBinding.etPayAmount.text?.clear()
                    userDetailBinding.etNotes.text?.clear()

                    // debit amount from logged-in user
                    withContext(Dispatchers.IO) {
                        mViewModel.debitAmount()
                        getAmountDebitObserver()
                    }
                }

                if (result.error != null) {
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }

    // Debit amount from logged-in user after transaction amount
    private suspend fun getAmountDebitObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.amountDebitState.collectLatest { result ->
                if (result.deposit != null) {
                    if (result.deposit == false) {
                        Log.e("PayUserFragment::", resources.getString(R.string.unable_to_debit_amount))
                    } else {
                        Log.e("PayUserFragment::", resources.getString(R.string.amount_debit_success))
                    }
                }
            }
        }
    }

}