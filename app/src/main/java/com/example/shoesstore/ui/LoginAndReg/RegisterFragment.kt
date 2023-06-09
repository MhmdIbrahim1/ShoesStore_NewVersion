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
import androidx.navigation.fragment.findNavController
import com.example.shoesstore.databinding.FragmentRegisterBinding
import com.example.shoesstore.viewmodels.DataViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerBtn.setOnClickListener {
            val userName = binding.UserNameEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill all the fields or Back", Toast.LENGTH_SHORT)
                    .show()
            } else if (password.length < 6) {
                binding.passwordEt.error = "Password must be at least 6 characters"
            }
            else {
                registerUser()
            }
            // check if the user is already registered
            checkUser()
        }

    }

    private fun registerUser() {
        val email = binding.UserNameEt.text.toString()
        val password = binding.passwordEt.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all the fields or go back", Toast.LENGTH_SHORT).show()
        } else {
            val firebaseManager = FirebaseManager()
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    firebaseManager.registerUser(email, password)
                    withContext(Dispatchers.Main) {
                        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                        findNavController().navigate(action)
                        Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.UserNameEt.error = "Registration failed!! Invalid Email or Password"
                    }
                }
            }
        }
    }

    // check if the user is already registered
    private fun checkUser() {
        // if the email is already registered in firebase show error
        val firebaseManager = FirebaseManager()
        val currentUser = firebaseManager.getCurrentUser()
        if (currentUser != null) {
            Toast.makeText(context, "Email Already Found!!", Toast.LENGTH_SHORT).show()
        }
    }
}

