package com.dm.todok.ui.authentification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.dm.todok.R
import com.dm.todok.databinding.FragmentAuthenticationBinding


class AuthenticationFragment : Fragment() {
    private var _binding: FragmentAuthenticationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logInButton.setOnClickListener {
            findNavController(this).navigate(R.id.action_authenticationFragment_to_loginFragment)
        }

        binding.signInButton.setOnClickListener {
            findNavController(this).navigate(R.id.action_authenticationFragment_to_signupFragment)
        }
    }
}