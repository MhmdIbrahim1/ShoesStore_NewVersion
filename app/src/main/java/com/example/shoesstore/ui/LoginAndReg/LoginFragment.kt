package com.example.shoesstore.ui.LoginAndReg

import com.example.shoesstore.FirebaseAuth.FirebaseManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.shoesstore.databinding.FragmentLoginBinding
import com.example.shoesstore.viewmodels.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var dataViewModel: DataViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            val email = binding.userNameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val firebaseManager = FirebaseManager()
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        firebaseManager.loginUser(email, password)
                        withContext(Dispatchers.Main) {
                            val action = LoginFragmentDirections.actionLoginFragmentToWelcomeFragment()
                            findNavController().navigate(action)
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            binding.userNameEditText.error = "Login failed!! Invalid Email or Password"
                        }
                    }
                }
            }
        }

        binding.regBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

}