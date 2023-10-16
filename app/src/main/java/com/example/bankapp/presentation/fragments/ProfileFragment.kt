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
import com.example.bankapp.databinding.FragmentProfileBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.toolbarTitle.text = resources.getString(R.string.Profile)

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

    // get logged-in user details
    private suspend fun getUserDetailObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.getUser.collectLatest { result ->

                if (result.error != null) {
                    binding.pbProfile.visibility = View.GONE
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                if (result.isLoading) {
                    binding.pbProfile.visibility = View.VISIBLE
                }

                result.userData?.collectLatest { user ->
                    with(binding) {
                        pbProfile.visibility = View.GONE
                        tvUsername.text = user.name
                        tvEmail.text = user.email
                        tvBankName.text = user.bankName
                        tvIfsc.text = user.ifscCode
                        tvTotalAmount.text = user.amount
                        tvAccountNumber.text = user.accountNumber
                        tvNameCard.text = user.name
                    }
                }
            }
        }
    }
}