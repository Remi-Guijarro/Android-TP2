package com.dm.todok.ui.authentification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.PreferenceManager
import com.dm.todok.R
import com.dm.todok.SHARED_PREF_TOKEN_KEY
import com.dm.todok.data.UserRepository
import com.dm.todok.databinding.FragmentLoginBinding
import com.dm.todok.form.LoginForm
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val userRepository = UserRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.connectionButton.setOnClickListener {
            val email = binding.editTextTextEmailAddress.text.toString()
            val pwd = binding.editTextTextPassword.text.toString()
            when {
                email.isEmpty() -> {
                    Toast.makeText(this.context, " you need to specify email to connect!", Toast.LENGTH_SHORT).show()
                }
                pwd.isEmpty() -> {
                    Toast.makeText(this.context, " you need to specify password to connect!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val loginForm = LoginForm(email, pwd)

                    val fragment = this
                    lifecycleScope.launch {
                        val loginResponse = userRepository.login(loginForm)
                        if (loginResponse != null) {
                            val fetchedToken = loginResponse.token
                            PreferenceManager.getDefaultSharedPreferences(context).edit {
                                putString(SHARED_PREF_TOKEN_KEY, fetchedToken)
                            }
                            NavHostFragment.findNavController(fragment).navigate(R.id.action_loginFragment_to_taskListFragment)
                        } else {
                            Toast.makeText(context, "An error occured during connection", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}