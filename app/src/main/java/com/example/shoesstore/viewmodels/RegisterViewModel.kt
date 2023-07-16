package com.example.shoesstore.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoesstore.model.UserModel.User
import com.example.shoesstore.util.Constants.USER_COLLECTION
import com.example.shoesstore.util.NetworkResult
import com.example.shoesstore.util.RegisterFailedState
import com.example.shoesstore.util.RegisterValidation
import com.example.shoesstore.util.validateEmail
import com.example.shoesstore.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<NetworkResult<User>>(NetworkResult.UnSpecified())
    val register: Flow<NetworkResult<User>> = _register

    private val _validation = Channel<RegisterFailedState>()
    val validation = _validation.receiveAsFlow()


    fun createAccountWithEmailAndPassword(user: User) {
        if (checkValidation(user)) {
            runBlocking {
                _register.emit(NetworkResult.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnSuccessListener {
                    it.user?.let { firebaseUser ->
                        saveUserInfo(firebaseUser.uid, user)
                    }
                }
                .addOnFailureListener {
                    _register.value = NetworkResult.Error(it.message.toString())
                }
        }else{
            val registerFailedState= RegisterFailedState(
                validateEmail(user.email),
                validatePassword(user.password)
            )
            viewModelScope.launch {
                _validation.send(registerFailedState)
            }
        }
    }


    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = NetworkResult.Success(user)
            }
            .addOnFailureListener {
                _register.value = NetworkResult.Error(it.message.toString())
            }
    }


    private fun checkValidation(user: User): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(user.password)
        return emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
    }
}