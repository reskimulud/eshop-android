package com.mankart.eshop.profile.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.utils.Constants.EXTRA_TRANSACTION_ID
import com.mankart.eshop.profile.R
import com.mankart.eshop.profile.databinding.FragmentProfileBinding
import com.mankart.eshop.profile.ui.ProfileViewModel
import com.mankart.eshop.profile.ui.adapter.ListTransactionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.URLEncoder

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTransactions.layoutManager = LinearLayoutManager(requireActivity())

        setupProfile()
        getTransactions()
    }

    private fun getTransactions() {
        lifecycleScope.launch {
            profileViewModel.getTransactions().collect {
                if (it is Resource.Success) {
                    if (!it.data.isNullOrEmpty()) {
                        val adapter = it.data?.let { listTransaction ->
                            ListTransactionAdapter(listTransaction) { transactionId ->
                                navigateToDetailTransaction(transactionId)
                            }
                        }
                        binding.rvTransactions.adapter = adapter
                    } else {
                        binding.tvNoTransaction.visibility = View.VISIBLE
                        binding.rvTransactions.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun navigateToDetailTransaction(transactionId: String) {
        val mBundle = Bundle()
        mBundle.putString(EXTRA_TRANSACTION_ID, transactionId)
        findNavController().navigate(R.id.action_profileFragment_to_detailTransactionFragment, mBundle)
    }

    private fun setupProfile() {
        lifecycleScope.launch {
            profileViewModel.apply {
                getUserName().collect {
                    binding.tvUserName.text = it

                    val urlEncodeName = URLEncoder.encode(it, "UTF-8")
                    Glide.with(requireActivity())
                        .load("https://ui-avatars.com/api/?background=random&name=$urlEncodeName")
                        .into(binding.circleImageView)
                }
                getEmail().collect {
                    binding.tvUserEmail.text = it
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvTransactions.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}