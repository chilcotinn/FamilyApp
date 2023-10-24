package com.chilcotin.familyapp.fragments.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentSettingsBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment() : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }
    private val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            authButtonState()
            tvUser.text = FirebaseAuth.getInstance().currentUser?.displayName

            btGoogleLogin.setOnClickListener {
                signInLauncher.launch(signInIntent())
            }
            btLogout.setOnClickListener {
                signOut()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            binding.tvUser.text = user?.displayName
            Toast.makeText(context, context?.getString(R.string.sign_in_done), Toast.LENGTH_SHORT)
                .show()
            authButtonState()
        }
    }

    private fun signInIntent(): Intent {
        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Base_Theme_FamilyApp)
            .build()
    }

    private fun signOut() {
        context?.let {
            AuthUI.getInstance()
                .signOut(it)
                .addOnCompleteListener {
                    Toast.makeText(
                        context,
                        context?.getString(R.string.sign_out_done),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.tvUser.text = getString(R.string.not_authorized)
                    authButtonState()
                }
        }
    }

    private fun authButtonState() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            binding.btGoogleLogin.isEnabled = true
            binding.btLogout.isEnabled = false
        } else {
            binding.btGoogleLogin.isEnabled = false
            binding.btLogout.isEnabled = true
        }
    }
}