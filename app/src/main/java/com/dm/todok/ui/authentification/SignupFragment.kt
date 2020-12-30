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
import com.dm.todok.databinding.FragmentSignupBinding
import com.dm.todok.form.SignUpForm
import kotlinx.coroutines.launch

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val userRepository = UserRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInButton.setOnClickListener {
            val name = binding.signUpName.text.toString()
            val lastName = binding.signUpLastname.text.toString()
            val email = binding.signUpEmail.text.toString()
            val pwd = binding.signUpPassword.text.toString()
            val pwdConfirmation = binding.signUpPasswordConfirmation.text.toString()

            when {
                name.isEmpty() -> {
                    Toast.makeText(this.context, "You need to specify name to sign up!", Toast.LENGTH_SHORT).show()
                }
                lastName.isEmpty() -> {
                    Toast.makeText(this.context, "You need to specify last name to sign up!", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val signUpForm = SignUpForm(name, lastName, email, pwd, pwdConfirmation)

                    val fragment = this
                    lifecycleScope.launch {
                        val responses = userRepository.signUp(signUpForm)
                        if (responses.second != null && !responses.second!!.errors.isNullOrEmpty()) {
                            Toast.makeText(context, responses.second!!.errors[0], Toast.LENGTH_SHORT).show()
                        } else {
                            val fetchedToken = responses.first?.token
                            PreferenceManager.getDefaultSharedPreferences(context).edit {
                                putString(SHARED_PREF_TOKEN_KEY, fetchedToken)
                            }
                            NavHostFragment.findNavController(fragment).navigate(R.id.action_signupFragment_to_taskListFragment)
                        }
                    }
                }
            }
        }
    }
}