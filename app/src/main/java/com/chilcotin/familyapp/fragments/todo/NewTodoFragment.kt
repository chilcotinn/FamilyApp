package com.chilcotin.familyapp.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentNewTodoBinding
import com.chilcotin.familyapp.entities.TodoItem
import com.chilcotin.familyapp.utils.Const
import com.chilcotin.familyapp.utils.InputManager
import com.chilcotin.familyapp.utils.TimeManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTodoFragment : Fragment() {
    private var _binding: FragmentNewTodoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewTodoBinding.inflate(layoutInflater, container, false)
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
                    bundle.putParcelable(Const.NEW_TODO, createNewTodoItem())
                    setFragmentResult(Const.NEW_TODO_REQUEST, bundle)
                    findNavController().navigate(R.id.action_newTodoFragment_to_todoFragment)
                } else {
                    edTitle.error = context?.getString(R.string.empty_filed)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createNewTodoItem(): TodoItem {
        return TodoItem(
            binding.edTitle.text.toString(),
            binding.edDescription.text.toString(),
            TimeManager.getTime(),
            false,
        )
    }
}