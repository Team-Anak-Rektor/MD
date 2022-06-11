package com.bintang.bangkitcapstoneproject.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bintang.bangkitcapstoneproject.databinding.FragmentProfileBinding
import com.bintang.bangkitcapstoneproject.ui.auth.login.LoginActivity
import com.bintang.bangkitcapstoneproject.utils.SessionPreferences
import com.bintang.bangkitcapstoneproject.ui.utils.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val pref = SessionPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref = pref, context = requireContext())
        )[ProfileViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewModel.getUserId().observe(viewLifecycleOwner) {
                tvUserId.text = it.toString()
            }

            viewModel.getUserName().observe(viewLifecycleOwner) {
                tvName.text = it.toString()
            }

            viewModel.getUserEmail().observe(viewLifecycleOwner) {
                tvEmail.text = it.toString()
            }

            btnLogout.setOnClickListener {
                val i = Intent(requireContext(), LoginActivity::class.java)
                restoreData()
                startActivity(i)
                requireActivity().finish()
            }
        }
    }

    private fun restoreData() {
        viewModel.setLoginSession(false)
        viewModel.setPrivateKey("")
        viewModel.setUserId("")
        viewModel.setUserName("")
        viewModel.setUserEmail("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}