package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.adapters.ShopListAdapter
import com.chilcotin.familyapp.databinding.FragmentShopListBinding
import com.chilcotin.familyapp.entity.ShopListItem
import com.chilcotin.familyapp.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopListFragment : Fragment(), ShopListAdapter.OnItemClickListener {
    private var _binding: FragmentShopListBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ShopListAdapter(this) }

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
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

        binding.fbAddShopList.setOnClickListener {
            findNavController().navigate(R.id.action_shopListFragment_to_newShopListItemFragment)
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

    override fun onItemClick(shopListItem: ShopListItem) {
    }
}