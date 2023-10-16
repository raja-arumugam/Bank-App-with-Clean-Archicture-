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
import androidx.navigation.fragment.findNavController
import com.example.bankapp.R
import com.example.bankapp.common.SingleToast
import com.example.bankapp.databinding.FragmentTransactionListBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.adapter.TransactionsListAdapter
import com.example.bankapp.presentation.adapter.UsersAdapter
import com.example.bankapp.presentation.viewmodel.TransactionListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionListFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentTransactionListBinding
    private lateinit var mViewModel: TransactionListViewModel
    private lateinit var mAdapter: TransactionsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_list, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.toolbarTitle.text = resources.getString(R.string.transactions)

        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.getAllTransactionsList()
            getTransactionListObserver()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.getLoggedInEmail()
            mViewModel.loggedInEmail.collectLatest { email ->
                if (email != "") {
                    mViewModel.getLoggedUserData(email)
                    getLoggedUserDetailObserver()
                }
            }
        }
    }

    private suspend fun getLoggedUserDetailObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.getLoggedUser.collectLatest { result ->

                if (result.error != null) {
                    binding.pbTransaction.visibility = View.GONE
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                if (result.isLoading) {
                    binding.pbTransaction.visibility = View.VISIBLE
                }

                result.userData?.collectLatest { user ->
                    with(binding) {
                        pbTransaction.visibility = View.GONE

                    }
                }
            }
        }
    }

    private suspend fun getTransactionListObserver(){
        withContext(Dispatchers.Main) {

            mViewModel.transactionList.collectLatest { result ->
                if (result.error != null) {
                    binding.pbTransaction.visibility = View.GONE
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                if (result.isLoading) {
                    binding.pbTransaction.visibility = View.VISIBLE
                }

                result.transaction?.collectLatest { list ->
                    binding.pbTransaction.visibility = View.GONE

                    mAdapter = TransactionsListAdapter(list) {
                        /*if (findNavController().currentDestination?.id == R.id.homeFragment) findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToUserDetailFragment(it)
                        )*/
                    }
                    binding.rvTransactionList.adapter = mAdapter
                }
            }
        }
    }
}