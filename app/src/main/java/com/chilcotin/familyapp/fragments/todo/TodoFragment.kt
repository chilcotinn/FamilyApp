package com.chilcotin.familyapp.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.MainViewModel
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentTodoBinding
import com.chilcotin.familyapp.db.TodoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TodoAdapter

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            initRcView()
        }

        binding.fbAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_todoFragment_to_newTodoFragment, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private suspend fun initRcView() = with(binding) {
        rcTodoList.layoutManager = LinearLayoutManager(requireContext())
        adapter = TodoAdapter()
        rcTodoList.adapter = adapter
        lifecycle.coroutineScope.launch(Dispatchers.Main) {
            mainViewModel.getAllTodoItem().collect {
                adapter.setList(it)
            }
        }
    }
}