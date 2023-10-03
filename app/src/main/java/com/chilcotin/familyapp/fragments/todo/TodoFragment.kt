package com.chilcotin.familyapp.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentTodoBinding
import com.chilcotin.familyapp.db.TodoAdapter
import com.chilcotin.familyapp.entity.TodoItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TodoAdapter

    private val todoList = mutableListOf<TodoItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todoList.add(
            TodoItem(
                1,
                "Test",
                "Description",
                "18:00 21.09",
                true
            )
        )
        todoList.add(
            TodoItem(
                2,
                "Test2",
                "Description 2",
                "18:00 21.09",
                false
            )
        )
        initRcView()

        binding.fbAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_todoFragment_to_newTodoFragment, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        rcTodoList.layoutManager = LinearLayoutManager(requireContext())
        adapter = TodoAdapter(todoList)
        rcTodoList.adapter = adapter
    }
}