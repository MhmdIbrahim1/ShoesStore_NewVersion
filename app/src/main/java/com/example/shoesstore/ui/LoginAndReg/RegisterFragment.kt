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
    private lateinit var dataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check if the user is already registered
        binding.registerBtn.setOnClickListener {
            val userName = binding.UserNameEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill all the fields or Back", Toast.LENGTH_SHORT)
                    .show()
            } else {
                registerUser()
            }
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

}

