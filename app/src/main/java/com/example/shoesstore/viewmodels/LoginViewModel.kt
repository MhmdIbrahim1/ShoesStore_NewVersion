package com.example.shoesstore.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesstore.R
import com.example.shoesstore.util.Constants.LOGIN_VALUE
import com.example.shoesstore.util.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferences: SharedPreferences,

    ) : ViewModel() {
    private val _navigateState = MutableStateFlow(0)
    val navigateState: StateFlow<Int> = _navigateState
    companion object{
        const val SHOES_FRAGMENT = 23
        val ACCOUNT_LOGIN_FRAGMENT get() = R.id.action_shoeInfoFragment_to_shoeListFragment
    }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(LOGIN_VALUE, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            viewModelScope.launch {
                _navigateState.emit(SHOES_FRAGMENT) // Navigate to ShoeListFragment
            }
        } else if (isButtonClicked) {
            viewModelScope.launch {
                _navigateState.emit(ACCOUNT_LOGIN_FRAGMENT) // Navigate to AccountLoginFragment
            }
        }

    }

    private val _login = MutableSharedFlow<NetworkResult<String>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<NetworkResult<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    private val firestore = FirebaseFirestore.getInstance()


    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            viewModelScope.launch {
                _login.emit(NetworkResult.Error("Email or password cannot be empty"))
            }
            return
        }
        viewModelScope.launch { _login.emit(NetworkResult.Loading()) }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                viewModelScope.launch {
                    it.user?.let {
                        handleLoginSuccess()
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _login.emit(NetworkResult.Error(it.message.toString()))
                }
            }
    }

    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                viewModelScope.launch {
                    authResult.user?.let { user ->
                        val userData = hashMapOf(
                            "uid" to user.uid,
                            "email" to user.email,
                            "displayName" to user.displayName,
                            "photoUrl" to user.photoUrl.toString()
                        )

                        firestore.collection("users")
                            .document(user.uid)
                            .set(userData)
                            .addOnSuccessListener {
                                viewModelScope.launch {
                                    _login.emit(NetworkResult.Success("Login Success"))
                                }
                            }
                            .addOnFailureListener {
                                viewModelScope.launch {
                                    _login.emit(NetworkResult.Error("Failed to add user data"))
                                }
                            }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _login.emit(NetworkResult.Error(it.message.toString()))
                }
            }
    }
    private fun startButtonClicked() {
        sharedPreferences.edit().putBoolean(LOGIN_VALUE, true).apply()
    }

    private fun handleLoginSuccess() {
        viewModelScope.launch {
            _login.emit(NetworkResult.Success("Login Success"))
            startButtonClicked() // Save the login state here
        }
    }
}