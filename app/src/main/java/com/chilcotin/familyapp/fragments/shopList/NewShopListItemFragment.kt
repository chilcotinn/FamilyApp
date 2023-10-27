package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chilcotin.familyapp.databinding.FragmentNewShopListItemBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewShopListItemFragment : Fragment() {
    private var _binding: FragmentNewShopListItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewShopListItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}