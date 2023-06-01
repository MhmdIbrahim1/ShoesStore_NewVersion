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
//            val userName = binding.userNameEditText.text.toString()
//            val password = binding.passwordEditText.text.toString()

//            dataViewModel.viewModelScope.launch {
//                val user = withContext(Dispatchers.IO) {
//                    dataViewModel.getUser(userName, password)
//                }
//                if (user != null) {
                val action = LoginFragmentDirections.actionLoginFragmentToWelcomeFragment()
                findNavController().navigate(action)
//                } else {
//                    binding.userNameEditText.error = "User not found"
//                    Toast.makeText(context, "Register or enter a valid username and password ", Toast.LENGTH_SHORT).show()
//                }
            }
        //}


        binding.regBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

    }
}