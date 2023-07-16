package com.example.shoesstore.ui.LoginAndReg

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentRegisterBinding
import com.example.shoesstore.util.NetworkResult
import com.example.shoesstore.util.RegisterValidation
import com.example.shoesstore.viewmodels.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.buttonRegisterRegister
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
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
        binding.tvDoYouHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }


        binding.apply {
            buttonRegisterRegister.setOnClickListener {
                val user = com.example.shoesstore.model.UserModel.User(
                    edFirstNameRegister.text.toString().trim(),
                    edLastNameRegister.text.toString().trim(),
                    edEmailRegister.text.toString().trim(),
                    edPasswordRegister.text.toString()
                )
                viewModel.createAccountWithEmailAndPassword(user)
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.register.collect() {
                when (it) {
                    is NetworkResult.Loading ->{
                        binding.buttonRegisterRegister.startAnimation()
                    }

                    is NetworkResult.Success ->{
                        Log.d("RegisterFragment", it.data.toString())
                        buttonRegisterRegister.revertAnimation()
                        binding.apply {
                            edFirstNameRegister.setText("")
                            edLastNameRegister.setText("")
                            edEmailRegister.setText("")
                            edPasswordRegister.setText("")
                        }
                        // Navigate to login fragment
                        Snackbar.make(requireView(), "Register Success", Snackbar.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    is NetworkResult.Error ->{
                        buttonRegisterRegister.revertAnimation()
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }

            }
        }

        lifecycleScope.launch {
            viewModel.validation.collect{validation ->
                if (validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edEmailRegister.apply {
                            error = validation.email.message
                            requestFocus()
                        }
                    }
                }

                if (validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPasswordRegister.apply {
                            error = validation.password.message
                            requestFocus()
                        }
                    }
                }

            }
        }

    }
}



