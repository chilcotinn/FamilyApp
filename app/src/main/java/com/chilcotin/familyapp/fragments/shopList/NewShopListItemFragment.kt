package com.chilcotin.familyapp.fragments.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentNewShopListItemBinding
import com.chilcotin.familyapp.entities.ShopListItem
import com.chilcotin.familyapp.utils.Const
import com.chilcotin.familyapp.utils.InputManager
import com.chilcotin.familyapp.utils.TimeManager
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
            InputManager.showSoftKeyboard(edTitle, requireContext())

            fbOkShopListItem.setOnClickListener {
                if (edTitle.text.isNotEmpty()) {
                    val bundle = Bundle()
                    bundle.putParcelable(Const.NEW_SHOP_LIST_ITEM, createNewShopListItem())
                    setFragmentResult(Const.NEW_SHOP_LIST_ITEM_REQUEST, bundle)
                    findNavController().navigate(R.id.action_newShopListItemFragment_to_shopListFragment)
                } else {
                    edTitle.error = getString(R.string.empty_filed)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun createNewShopListItem(): ShopListItem {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user != null) {
            ShopListItem(
                binding.edTitle.text.toString(),
                binding.edDescription.text.toString(),
                TimeManager.getTime(),
                user.displayName.toString(),
            )
        } else {
            ShopListItem(
                binding.edTitle.text.toString(),
                binding.edDescription.text.toString(),
                TimeManager.getTime(),
                getString(R.string.not_authorized),
            )
        }
    }
}