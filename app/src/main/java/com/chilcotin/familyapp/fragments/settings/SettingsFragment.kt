package com.chilcotin.familyapp.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentSettingsBinding
import com.chilcotin.familyapp.firebase.Authorization
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        Authorization().onSignInResult(res, requireContext())
        checkAuthState()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAuthState()

        binding.btGoogleLogin.setOnClickListener {
            signInLauncher.launch(Authorization().signInIntent())
        }
        binding.btLogout.setOnClickListener {
            signOut()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkAuthState() {
        val user = FirebaseAuth.getInstance().currentUser
        binding.apply {
            if (user != null) {
                btGoogleLogin.isEnabled = false
                btLogout.isEnabled = true
                tvUser.text = user.displayName
            } else {
                btGoogleLogin.isEnabled = true
                btLogout.isEnabled = false
                tvUser.text = getString(R.string.not_authorized)
            }
        }
    }

    private fun signOut() {
        AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
            Toast.makeText(
                context,
                requireContext().getString(R.string.sign_out_done),
                Toast.LENGTH_SHORT
            ).show()
            checkAuthState()
        }
    }
}