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
import com.dm.todok.databinding.FragmentLoginBinding
import com.dm.todok.model.LoginForm
import com.dm.todok.network.Api
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.connectionButton.setOnClickListener {
            if(binding.editTextTextEmailAddress.text.toString().isEmpty()){
                Toast.makeText(this.context, " you need to specify email to connect !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (binding.editTextTextPassword.text.toString().isEmpty()) {
                Toast.makeText(this.context, " you need to specify password to connect !", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var loginForm = LoginForm(binding.editTextTextEmailAddress.text.toString(), binding.editTextTextPassword.text.toString())

            val context = context
            val fragment = this
            lifecycleScope.launch {
                var response = Api.INSTANCE.userWebService.login(loginForm)
                if(response.isSuccessful) {
                    PreferenceManager.getDefaultSharedPreferences(context).edit {
                        putString(SHARED_PREF_TOKEN_KEY, response.body()?.token)
                    }
                    NavHostFragment.findNavController(fragment).navigate(R.id.action_loginFragment_to_taskListFragment)
                } else{
                    Toast.makeText(context, "An error ocured during connection", Toast.LENGTH_SHORT).show()
                    return@launch
                }
            }
        }
    }
}