package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.adapters.ShopItemAdapter
import com.chilcotin.familyapp.databinding.FragmentShopItemsBinding
import com.chilcotin.familyapp.entities.ShopItem
import com.chilcotin.familyapp.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopItemsFragment : Fragment(), ShopItemAdapter.OnItemClickListener {
    private var _binding: FragmentShopItemsBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ShopItemAdapter(this) }
    private val args: ShopItemsFragmentArgs by navArgs()

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRcView()
        observer()

        val itemTouchHelperCallback = createItemTouchHelper()
        itemTouchHelperCallback.attachToRecyclerView(binding.rcShopItems)

        binding.apply {
            ibOk.setOnClickListener {
                if (edShopItem.text.isNotEmpty()) {
                    mainViewModel.insertShopItem(createNewShopItem())
                    edShopItem.setText("")
                } else {
                    edShopItem.error = getString(R.string.empty_filed)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.itemEvent.collect { event ->
                    when (event) {
                        is MainViewModel.ItemEvent.ShowUndoDeleteShopItemMessage -> {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.deleted),
                                Snackbar.LENGTH_LONG
                            ).setAction(getString(R.string.undo)) {
                                mainViewModel.onShopItemUndoDeleteClick(event.shopItem)
                            }.show()
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
        rcShopItems.layoutManager = LinearLayoutManager(requireContext())
        rcShopItems.adapter = adapter
        rcShopItems.setHasFixedSize(true)
    }

    private fun observer() {
        lifecycle.coroutineScope.launch {
            mainViewModel.getAllShopItem(args.shopListItemId).observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    private fun createNewShopItem(): ShopItem {
        return ShopItem(
            binding.edShopItem.text.toString(),
            false,
            args.shopListItemId
        )
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
                    mainViewModel.deleteShopItem(item)
                }
            }
        )
    }

    override fun onCheckedBoxClick(shopItem: ShopItem, isChecked: Boolean) {
        mainViewModel.onShopItemCheckedChanged(shopItem, isChecked)
    }
}