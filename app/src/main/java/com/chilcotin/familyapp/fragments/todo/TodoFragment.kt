package com.chilcotin.familyapp.fragments.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.Const.NEW_TODO
import com.chilcotin.familyapp.Const.NEW_TODO_REQUEST
import com.chilcotin.familyapp.viewModel.MainViewModel
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentTodoBinding
import com.chilcotin.familyapp.db.TodoAdapter
import com.chilcotin.familyapp.entity.TodoItem
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(NEW_TODO_REQUEST) { _, bundle ->
            val result: TodoItem = bundle.getSerializable(NEW_TODO) as TodoItem
            mainViewModel.insertTodoItem(result)
        }
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

        initRcView()

        val itemTouchHelperCallback = ItemTouchHelper(
            object :
                ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val todoItem = adapter.getTodoItem(position)
                    mainViewModel.deleteTodoItem(todoItem)
                }
            })

        itemTouchHelperCallback.attachToRecyclerView(binding.rcTodoList)

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
        adapter = TodoAdapter()
        rcTodoList.adapter = adapter
        lifecycle.coroutineScope.launch(Dispatchers.Main) {
            mainViewModel.getAllTodoItem().collect {
                adapter.setList(it)
            }
        }
    }
}