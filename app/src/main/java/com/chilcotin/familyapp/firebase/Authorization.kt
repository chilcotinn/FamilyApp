package com.chilcotin.familyapp.firebase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.chilcotin.familyapp.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult

class Authorization {
    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )

    fun signInIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Base_Theme_FamilyApp)
            .build()
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult, context: Context) {
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(context, context.getString(R.string.sign_in_done), Toast.LENGTH_SHORT)
                .show()
        }
    }
}