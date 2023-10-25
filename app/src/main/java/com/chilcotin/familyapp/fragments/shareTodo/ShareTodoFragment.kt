package com.chilcotin.familyapp.fragments.shareTodo

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
import com.chilcotin.familyapp.adapters.ShareTodoAdapter
import com.chilcotin.familyapp.databinding.FragmentShareTodoBinding
import com.chilcotin.familyapp.entity.ShareTodoItem
import com.chilcotin.familyapp.utils.Const.NEW_SHARE_TODO
import com.chilcotin.familyapp.utils.Const.NEW_SHARE_TODO_REQUEST
import com.chilcotin.familyapp.utils.Const.UPDATE_SHARE_TODO
import com.chilcotin.familyapp.utils.Const.UPDATE_SHARE_TODO_REQUEST
import com.chilcotin.familyapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShareTodoFragment : Fragment(), ShareTodoAdapter.OnItemClickListener {
    private var _binding: FragmentShareTodoBinding? = null
    private val binding get() = _binding!!
    val adapter by lazy { ShareTodoAdapter(this) }

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(NEW_SHARE_TODO_REQUEST) { _, bundle ->
            val result: ShareTodoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(NEW_SHARE_TODO, ShareTodoItem::class.java)
                    ?: ShareTodoItem(getString(R.string.error))
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(NEW_SHARE_TODO) ?: ShareTodoItem(getString(R.string.error))
            }
            mainViewModel.insertShareTodoItem(result)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShareTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRcView()
        observer()

        val itemTouchHelperCallback = createItemTouchHelper()
        itemTouchHelperCallback.attachToRecyclerView(binding.rcShareTodoList)

        binding.fbAddShareTask.setOnClickListener {
            findNavController().navigate(R.id.action_shareTodoFragment_to_newShareTodoFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.itemEvent.collect { event ->
                    when (event) {
                        is MainViewModel.ItemEvent.ShowUndoDeleteShareTodoItemMessage -> {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.deleted),
                                Snackbar.LENGTH_LONG
                            ).setAction(getString(R.string.undo)) {
                                mainViewModel.onShareTodoItemUndoDeleteClick(event.shareTodoItem)
                            }.show()
                        }

                        is MainViewModel.ItemEvent.NavigateToEditShareTodoItemScreen -> {
                            val bundle = Bundle()
                            bundle.putParcelable(UPDATE_SHARE_TODO, event.shareTodoItem)
                            setFragmentResult(UPDATE_SHARE_TODO_REQUEST, bundle)
                            findNavController().navigate(R.id.action_shareTodoFragment_to_editShareTodoFragment)
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
        rcShareTodoList.layoutManager = LinearLayoutManager(requireContext())
        rcShareTodoList.adapter = adapter
        rcShareTodoList.setHasFixedSize(true)
    }

    private fun observer() {
        lifecycle.coroutineScope.launch {
            mainViewModel.getAllShareTodoItem().observe(viewLifecycleOwner) {
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
                    mainViewModel.deleteShareTodoItem(item)
                }
            }
        )
    }

    override fun onItemClick(shareTodoItem: ShareTodoItem) {
        mainViewModel.onShareTodoItemSelected(shareTodoItem)
    }

    override fun onCheckedBoxClick(shareTodoItem: ShareTodoItem, isChecked: Boolean) {
        mainViewModel.onShareTodoItemCheckedChanged(shareTodoItem, isChecked)
    }
}