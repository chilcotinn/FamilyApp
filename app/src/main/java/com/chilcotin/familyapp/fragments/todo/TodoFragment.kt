package com.chilcotin.familyapp.fragments.todo

import android.os.Build
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
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentTodoBinding
import com.chilcotin.familyapp.db.TodoAdapter
import com.chilcotin.familyapp.entity.TodoItem
import com.chilcotin.familyapp.utils.Const.NEW_TODO
import com.chilcotin.familyapp.utils.Const.NEW_TODO_REQUEST
import com.chilcotin.familyapp.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoFragment : Fragment() {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    val adapter by lazy { TodoAdapter() }

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(NEW_TODO_REQUEST) { _, bundle ->
            val result: TodoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(NEW_TODO, TodoItem::class.java) ?: TodoItem("Error")
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(NEW_TODO) ?: TodoItem("Error")
            }
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
        observer()

        val itemTouchHelperCallback = createItemTouchHelper()
        itemTouchHelperCallback.attachToRecyclerView(binding.rcTodoList)

        binding.fbAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_todoFragment_to_newTodoFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        rcTodoList.layoutManager = LinearLayoutManager(requireContext())
        rcTodoList.adapter = adapter
        rcTodoList.setHasFixedSize(true)
    }

    private fun observer() {
        lifecycle.coroutineScope.launch {
            mainViewModel.getAllTodoItem().observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        return ItemTouchHelper(
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
                    val item = adapter.currentList[position]
                    mainViewModel.deleteTodoItem(item)
                }
            })
    }
}