package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentNewShopListItemBinding
import com.chilcotin.familyapp.entity.ShopListItem
import com.chilcotin.familyapp.utils.Const.NEW_SHOP_LIST_ITEM
import com.chilcotin.familyapp.utils.Const.NEW_SHOP_LIST_ITEM_REQUEST
import com.chilcotin.familyapp.utils.TimeManager.getTime
import com.google.firebase.auth.FirebaseAuth
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            edTitle.requestFocus()
            showSoftKeyboard(edTitle)
        }

        binding.fbOkShopListItem.setOnClickListener {
            if (binding.edTitle.text.isNotEmpty()) {
                val bundle = Bundle()
                bundle.putParcelable(NEW_SHOP_LIST_ITEM, createNewShopListItem())
                setFragmentResult(NEW_SHOP_LIST_ITEM_REQUEST, bundle)
                findNavController().navigate(R.id.action_newShopListItemFragment_to_shopListFragment)
            } else {
                binding.edTitle.error = getString(R.string.empty_filed)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createNewShopListItem(): ShopListItem {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            return ShopListItem(
                binding.edTitle.text.toString(),
                getTime(),
                user.displayName.toString(),
            )
        } else {
            return ShopListItem(
                binding.edTitle.text.toString(),
                getTime(),
                getString(R.string.not_authorized),
            )
        }
    }

    private fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = context?.getSystemService(InputMethodManager::class.java)
            imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}