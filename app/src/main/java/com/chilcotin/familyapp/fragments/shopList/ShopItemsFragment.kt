package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.adapters.ShopItemAdapter
import com.chilcotin.familyapp.databinding.FragmentShopItemsBinding
import com.chilcotin.familyapp.entities.ShopItem
import com.chilcotin.familyapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopItemsFragment : Fragment(), ShopItemAdapter.OnItemClickListener {
    private var _binding: FragmentShopItemsBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ShopItemAdapter(this) }

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

    override fun onCheckedBoxClick(shopItem: ShopItem, isChecked: Boolean) {
    }
}