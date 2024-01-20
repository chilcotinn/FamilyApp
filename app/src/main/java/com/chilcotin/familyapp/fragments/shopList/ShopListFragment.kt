package com.chilcotin.familyapp.fragments.shopList

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.adapters.ShopListAdapter
import com.chilcotin.familyapp.databinding.FragmentShopListBinding
import com.chilcotin.familyapp.entities.ShopListItem
import com.chilcotin.familyapp.utils.Const
import com.chilcotin.familyapp.viewmodel.MainViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopListFragment : Fragment(), ShopListAdapter.OnItemClickListener {
    private var _binding: FragmentShopListBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ShopListAdapter(this) }
    private val postListener = createValueEventListener()

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootPath = Firebase.database.getReference(getString(R.string.root_path_shop_list))

        setFragmentResultListener(Const.NEW_SHOP_LIST_ITEM_REQUEST) { _, bundle ->
            val result: ShopListItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(Const.NEW_SHOP_LIST_ITEM, ShopListItem::class.java)
                    ?: ShopListItem(getString(R.string.error))
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(Const.NEW_SHOP_LIST_ITEM)
                    ?: ShopListItem(getString(R.string.error))
            }
            mainViewModel.insertShopListItem(result, rootPath)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootPath = Firebase.database.getReference(getString(R.string.root_path_shop_list))

        initRcView()
        observer(rootPath)

        binding.fbAddShopList.setOnClickListener {
            findNavController().navigate(R.id.action_shopListFragment_to_newShopListItemFragment)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.itemEvent.collect { event ->
                    when (event) {
                        is MainViewModel.ItemEvent.NavigateToShopItemsScreen -> {
                            val action =
                                ShopListFragmentDirections.actionShopListFragmentToShopItemsFragment(
                                    event.shopListItem.title
                                )
                            findNavController().navigate(action)
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
        rootPath.removeEventListener(postListener)

        super.onDestroy()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        rcShopListItem.layoutManager = LinearLayoutManager(requireContext())
        rcShopListItem.adapter = adapter
        rcShopListItem.setHasFixedSize(true)
    }

    private fun observer(rootPath: DatabaseReference) {
        lifecycle.coroutineScope.launch {
            rootPath.addValueEventListener(postListener)
            mainViewModel.getAllShopListItem()
        }
    }

    private fun deleteAlertDialog(item: ShopListItem, rootPath: DatabaseReference) {
        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.create()
        dialog.setTitle(R.string.delete)

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok)) { _, _ ->
            mainViewModel.deleteShopListItem(item, rootPath)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createValueEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<ShopListItem>()

                for (item in snapshot.children) {
                    val shopListItem = item.getValue(ShopListItem::class.java)
                    if (shopListItem != null) list.add(shopListItem)
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onItemClick(shopListItem: ShopListItem) {
        mainViewModel.onShopListItemSelected(shopListItem)
    }

    override fun deleteItem(shopListItem: ShopListItem, rootPath: DatabaseReference) {
        deleteAlertDialog(shopListItem, rootPath)
    }
}