package com.example.shoesstore.util

import android.util.Patterns
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Context
import com.example.shoesstore.R

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty()) {
        return RegisterValidation.Failed("Email cannot be empty")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong email format")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty()) {
        return RegisterValidation.Failed("Password cannot be empty")
    }
    if (password.length < 6) {
        return RegisterValidation.Failed("Password must be at least 6 characters")
    }
    return RegisterValidation.Success
}

 fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .build()
    return GoogleSignIn.getClient(context, gso)
}

