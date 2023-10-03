package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chilcotin.familyapp.databinding.FragmentShopListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopListFragment : Fragment() {
    private var _binding: FragmentShopListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}