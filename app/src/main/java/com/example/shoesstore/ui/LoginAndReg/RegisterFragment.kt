package com.example.shoesstore.ui.LoginAndReg

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentRegisterBinding
import com.example.shoesstore.model.UserModel.User
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
    ): View? {
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
        val firstName = binding.FirstNameEt.text.toString()
        val lastName = binding.LastNameEt.text.toString()
        val userName = binding.UserNameEt.text.toString()
        val password = binding.passwordEt.text.toString()
        val user = User(
            firstName = firstName,
            lastName = lastName,
            userName = userName,
            password = password
        )

        dataViewModel.viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                dataViewModel.insertUser(user)
            }
        }
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
        Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()
    }
}

