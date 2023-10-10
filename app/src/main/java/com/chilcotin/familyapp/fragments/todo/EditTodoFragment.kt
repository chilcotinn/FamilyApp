package com.chilcotin.familyapp.fragments.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chilcotin.familyapp.databinding.FragmentEditTodoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTodoFragment : Fragment() {
    private var _binding: FragmentEditTodoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTodoBinding.inflate(layoutInflater, container, false)
        Log.d("MyLog", "onCreateView EditTodoFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fbOkTask.setOnClickListener {
            if (binding.checkBoxUpdateDate.isChecked) {
            } else {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("MyLog", "onDestroy EditTodoFragment")
    }
}