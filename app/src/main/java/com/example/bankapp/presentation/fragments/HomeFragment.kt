package com.example.bankapp.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bankapp.R
import com.example.bankapp.common.SingleToast
import com.example.bankapp.databinding.FragmentHomeBinding
import com.example.bankapp.di.components.app.Injectable
import com.example.bankapp.di.components.viewmodel.injectViewModel
import com.example.bankapp.presentation.activity.MainActivity
import com.example.bankapp.presentation.adapter.UsersAdapter
import com.example.bankapp.presentation.viewmodel.HomeViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var homeBinding: FragmentHomeBinding
    private var drawerLayout: DrawerLayout? = null
    private val navArgs: HomeFragmentArgs by navArgs()
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mAdapter: UsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        mViewModel = injectViewModel(viewModelFactory)
        homeBinding.lifecycleOwner = viewLifecycleOwner

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window? = activity?.window
            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window?.statusBarColor = ContextCompat.getColor(
                requireContext(),
                com.google.android.material.R.color.mtrl_btn_transparent_bg_color
            )
        }

        val email = navArgs.userEmail

        lifecycleScope.launch(Dispatchers.IO) {
            homeBinding.pbHome.visibility = View.VISIBLE
            mViewModel.getUserData(email)
            getUserDetailObserver()
        }

        with(homeBinding) {
            val toolbar = toolbar
            (activity as MainActivity).setSupportActionBar(toolbar)
            drawerLayout = drawer
            drawerLayout!!.addDrawerListener(
                ActionBarDrawerToggle(
                    requireActivity(),
                    drawerLayout,
                    toolbar,
                    R.string.cat_navigationdrawer_button_show_content_description,
                    R.string.cat_navigationdrawer_button_hide_content_description
                )
            )

            drawerLayout!!.addDrawerListener(
                object : SimpleDrawerListener() {
                    override fun onDrawerOpened(drawerView: View) {
                        drawerOnBackPressedCallback.isEnabled = true
                    }

                    override fun onDrawerClosed(drawerView: View) {
                        drawerOnBackPressedCallback.isEnabled = false
                    }
                })

            val navigationViewStart = homeBinding.navigationView
            initNavigationView(navigationViewStart)
        }

        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            mViewModel.InsertAllUsers()
            getInsertUsersObserver()
        }
    }

    private suspend fun getUserDetailObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.getUser.collectLatest { result ->

                if (result.error != null) {
                    homeBinding.pbHome.visibility = View.GONE
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                if (result.isLoading) {
                    homeBinding.pbHome.visibility = View.VISIBLE
                }

                result.userData?.collectLatest { user ->
                    with(homeBinding) {
                        pbHome.visibility = View.GONE

                        toolbar.title =
                            resources.getString(R.string.hello) + " " + user.name + resources.getString(
                                R.string.exclamatory_symbol
                            )

                        homeBinding.llCard.tvName.text = user.name
                        homeBinding.llCard.tvAccountNumber.text = user.accountNumber
                        homeBinding.llCard.tvAmount.text =
                            resources.getString(R.string.rs) + " " + user.amount

                        val userName = navigationView.getHeaderView(0)
                            .findViewById<View>(R.id.tv_username) as TextView
                        userName.text = user.name

                        val email = navigationView.getHeaderView(0)
                            .findViewById<View>(R.id.tv_email) as TextView
                        email.text = user.email

                        navigationView.getHeaderView(0).setOnClickListener {
                            val direction =
                                HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                            findNavController().navigate(direction)
                        }
                    }
                }
            }
        }
    }

    private suspend fun getUsersListObserver() {
        withContext(Dispatchers.Main) {
            mViewModel.getUsersList.collectLatest { result ->
                if (result.error != null) {
                    homeBinding.pbHome.visibility = View.GONE
                    SingleToast.show(
                        requireActivity(),
                        result.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }

                if (result.isLoading) {
                    homeBinding.pbHome.visibility = View.VISIBLE
                }

                result.usersList?.collectLatest { usersList ->
                    homeBinding.pbHome.visibility = View.GONE

                    mAdapter = UsersAdapter(usersList) {
                        if (findNavController().currentDestination?.id == R.id.homeFragment) findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToUserDetailFragment(it)
                        )
                    }
                    homeBinding.rvUsersList.adapter = mAdapter
                }
            }
        }
    }

    private suspend fun getInsertUsersObserver() {
        mViewModel.setUsersState.collectLatest { insertResult ->
            withContext(Dispatchers.Main) {
                if (insertResult.setUsers != null && insertResult.setUsers != 0) {
                    withContext(Dispatchers.IO) {
                        mViewModel.getAllUsersList()
                        getUsersListObserver()
                    }
                }

                if (insertResult.error != null) {
                    SingleToast.show(
                        requireActivity(),
                        insertResult.error.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
    }

    private fun initNavigationView(navigationView: NavigationView) {
        navigationView.setCheckedItem(R.id.item_transfers)
        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem? ->

            when (menuItem?.itemId) {
                R.id.item_transfers -> {
                    val direction =
                        HomeFragmentDirections.actionHomeFragmentToTransactionListFragment()
                    findNavController().navigate(direction)
                }

                R.id.item_deposits -> {
                    val direction = HomeFragmentDirections.actionHomeFragmentToDepositFragment()
                    findNavController().navigate(direction)
                }

                R.id.item_payments -> {

                }

                R.id.item_settings -> {

                }

                R.id.item_logout -> {
                    lifecycleScope.launch {
                        mViewModel.clearDataStore()
                    }
                    val direction = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
                    findNavController().navigate(direction)
                }
            }

            drawerLayout!!.closeDrawer(navigationView)
            true
        }
    }

    private val drawerOnBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                drawerLayout!!.closeDrawers()
            }
        }
}