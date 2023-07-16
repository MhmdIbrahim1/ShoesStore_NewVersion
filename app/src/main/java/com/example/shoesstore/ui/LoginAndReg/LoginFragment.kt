package com.example.shoesstore.ui.LoginAndReg

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shoesstore.R
import com.example.shoesstore.databinding.FragmentLoginBinding
import com.example.shoesstore.util.NetworkResult
import com.example.shoesstore.util.getGoogleSignInClient
import com.example.shoesstore.viewmodels.DataViewModel
import com.example.shoesstore.viewmodels.LoginViewModel
import com.example.shoesstore.viewmodels.LoginViewModel.Companion.ACCOUNT_LOGIN_FRAGMENT
import com.example.shoesstore.viewmodels.LoginViewModel.Companion.SHOES_FRAGMENT
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.tvDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = binding.edEmailLogin.text.toString().trim()
                val password = binding.edPasswordLogin.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(email, password)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Please enter the email and password!!",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.navigateState.collect { state ->
                if (state == SHOES_FRAGMENT) {
                    findNavController().navigate(R.id.action_loginFragment_to_shoeListFragment)
                } else if (state == ACCOUNT_LOGIN_FRAGMENT) {
                    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect() {
                when (it) {
                    is NetworkResult.Loading -> {
                        binding.buttonLoginLogin.startAnimation()
                    }

                    is NetworkResult.Success -> {
                        binding.buttonLoginLogin.revertAnimation()
                        withContext(Dispatchers.Main) {
                            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
                        }
                    }

                    is NetworkResult.Error -> {
                        binding.buttonLoginLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }

        binding.googleLogin.setOnClickListener {
            val signInClient = getGoogleSignInClient(requireContext())
            googleSignInLauncher.launch(signInClient.signInIntent)
        }


    }


    private val googleSignInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    viewModel.signInWithGoogle(it.idToken!!)
                }
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Google sign-in failed", Toast.LENGTH_LONG).show()
            }
        }

}