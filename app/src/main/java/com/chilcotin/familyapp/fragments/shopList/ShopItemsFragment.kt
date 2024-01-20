package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopItemsFragment : Fragment(), ShopItemAdapter.OnItemClickListener {
    private var _binding: FragmentShopItemsBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ShopItemAdapter(this) }
    private val args: ShopItemsFragmentArgs by navArgs()
    private val postListener = createValueEventListener()

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

        val rootPath = Firebase.database.getReference(getString(R.string.root_path_shop_list))
            .child(args.shopListTitle)

        initRcView()
        observer(rootPath)

        val itemTouchHelperCallback = createItemTouchHelper(rootPath)
        itemTouchHelperCallback.attachToRecyclerView(binding.rcShopItems)

        binding.apply {
            ibOk.setOnClickListener {
                if (edShopItem.text.isNotEmpty()) {
                    val shopItem = createNewShopItem()
                    mainViewModel.insertShopItem(shopItem, rootPath)
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
                                mainViewModel.onShopItemUndoDeleteClick(
                                    event.shopItem,
                                    rootPath
                                )
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
        val rootPath = Firebase.database.getReference(getString(R.string.root_path_shop_list))
            .child(args.shopListTitle)
        rootPath.removeEventListener(postListener)

        super.onDestroy()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        rcShopItems.layoutManager = LinearLayoutManager(requireContext())
        rcShopItems.adapter = adapter
        rcShopItems.setHasFixedSize(true)
    }

    private fun observer(rootPath: DatabaseReference) {
        lifecycle.coroutineScope.launch {
            rootPath.child(args.shopListTitle).addValueEventListener(postListener)
            mainViewModel.getAllShopItem()
        }
    }

    private fun createNewShopItem(): ShopItem {
        return ShopItem(
            binding.edShopItem.text.toString(),
            false,
            args.shopListTitle
        )
    }

    private fun createValueEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<ShopItem>()

                for (item in snapshot.children) {
                    val shopItem = item.getValue(ShopItem::class.java)
                    if (shopItem != null) list.add(shopItem)
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createItemTouchHelper(rootPath: DatabaseReference): ItemTouchHelper {
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
                    mainViewModel.deleteShopItem(item, rootPath)
                }
            }
        )
    }

    override fun onCheckedBoxClick(shopItem: ShopItem, rootPath: DatabaseReference) {
        mainViewModel.onShopItemCheckedChanged(shopItem, rootPath)
    }
}