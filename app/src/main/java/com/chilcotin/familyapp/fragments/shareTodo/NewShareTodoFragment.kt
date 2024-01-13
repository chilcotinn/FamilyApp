package com.chilcotin.familyapp.fragments.shareTodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentNewShareTodoBinding
import com.chilcotin.familyapp.entities.ShareTodoItem
import com.chilcotin.familyapp.utils.Const
import com.chilcotin.familyapp.utils.InputManager
import com.chilcotin.familyapp.utils.TimeManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewShareTodoFragment : Fragment() {
    private var _binding: FragmentNewShareTodoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewShareTodoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            edTitle.requestFocus()
            InputManager.showSoftKeyboard(edTitle, requireContext())

            fbOkTask.setOnClickListener {
                if (edTitle.text.isNotEmpty()) {
                    val bundle = Bundle()
                    bundle.putParcelable(Const.NEW_SHARE_TODO, createNewShareTodoItem())
                    setFragmentResult(Const.NEW_SHARE_TODO_REQUEST, bundle)
                    findNavController().navigate(R.id.action_newShareTodoFragment_to_shareTodoFragment)
                } else {
                    edTitle.error = getString(R.string.empty_filed)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createNewShareTodoItem(): ShareTodoItem {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            return ShareTodoItem(
                binding.edTitle.text.toString(),
                binding.edDescription.text.toString(),
                TimeManager.getTime(),
                false,
                user.displayName.toString()
            )
        } else {
            return ShareTodoItem(
                binding.edTitle.text.toString(),
                binding.edDescription.text.toString(),
                TimeManager.getTime(),
                false,
                getString(R.string.not_authorized)
            )
        }
    }
}