package com.chilcotin.familyapp.fragments.todo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.adapters.TodoAdapter
import com.chilcotin.familyapp.databinding.FragmentTodoBinding
import com.chilcotin.familyapp.entity.TodoItem
import com.chilcotin.familyapp.utils.Const.NEW_TODO
import com.chilcotin.familyapp.utils.Const.NEW_TODO_REQUEST
import com.chilcotin.familyapp.utils.Const.UPDATE_TODO
import com.chilcotin.familyapp.utils.Const.UPDATE_TODO_REQUEST
import com.chilcotin.familyapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoFragment : Fragment(), TodoAdapter.OnItemClickListener {
    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    val adapter by lazy { TodoAdapter(this) }

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(NEW_TODO_REQUEST) { _, bundle ->
            val result: TodoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(NEW_TODO, TodoItem::class.java)
                    ?: TodoItem(getString(R.string.error))
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(NEW_TODO) ?: TodoItem(getString(R.string.error))
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.itemEvent.collect { event ->
                    when (event) {
                        is MainViewModel.ItemEvent.ShowUndoDeleteTodoItemMessage -> {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.deleted),
                                Snackbar.LENGTH_LONG
                            ).setAction(R.string.undo) {
                                mainViewModel.onTodoItemUndoDeleteClick(event.todoItem)
                            }.show()
                        }

                        is MainViewModel.ItemEvent.NavigateToEditTodoItemScreen -> {
                            val bundle = Bundle()
                            bundle.putParcelable(UPDATE_TODO, event.todoItem)
                            setFragmentResult(UPDATE_TODO_REQUEST, bundle)
                            findNavController().navigate(R.id.action_todoFragment_to_editTodoFragment)
                        }

                        else -> {
                            Log.d("MyLog", getString(R.string.error))
                        }
                    }
                }
            }
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

    override fun onItemClick(todoItem: TodoItem) {
        mainViewModel.onTodoItemSelected(todoItem)
    }

    override fun onCheckBoxClick(todoItem: TodoItem, isChecked: Boolean) {
        mainViewModel.onTodoItemCheckedChanged(todoItem, isChecked)
    }
}