package com.chilcotin.familyapp.fragments.shopList

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopListFragment : Fragment(), ShopListAdapter.OnItemClickListener {
    private var _binding: FragmentShopListBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ShopListAdapter(this) }

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(Const.NEW_SHOP_LIST_ITEM_REQUEST) { _, bundle ->
            val result: ShopListItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(Const.NEW_SHOP_LIST_ITEM, ShopListItem::class.java)
                    ?: ShopListItem(getString(R.string.error))
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(Const.NEW_SHOP_LIST_ITEM)
                    ?: ShopListItem(getString(R.string.error))
            }
            mainViewModel.insertShopListItem(result)
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

        initRcView()
        observer()

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
                                    event.shopListItem.id
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
        super.onDestroy()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        rcShopListItem.layoutManager = LinearLayoutManager(requireContext())
        rcShopListItem.adapter = adapter
        rcShopListItem.setHasFixedSize(true)
    }

    private fun observer() {
        lifecycle.coroutineScope.launch {
            mainViewModel.getAllShopListItem().observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }
    }

    private fun deleteAlertDialog(item: ShopListItem) {
        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.create()
        dialog.setTitle(R.string.delete)

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok)) { _, _ ->
            mainViewModel.deleteShopListItem(item)
            mainViewModel.deleteShopItemById(item.id)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onItemClick(shopListItem: ShopListItem) {
        mainViewModel.onShopListItemSelected(shopListItem)
    }

    override fun deleteItem(shopListItem: ShopListItem) {
        deleteAlertDialog(shopListItem)
    }
}