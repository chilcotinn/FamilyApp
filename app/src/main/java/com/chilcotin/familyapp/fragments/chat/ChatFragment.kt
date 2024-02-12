package com.chilcotin.familyapp.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.adapters.ChatAdapter
import com.chilcotin.familyapp.databinding.FragmentChatBinding
import com.chilcotin.familyapp.entities.ChatItem
import com.chilcotin.familyapp.utils.TimeManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { ChatAdapter() }
    private val valueEventListener = createValueEventListener()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val rootPath = Firebase.database.getReference(getString(R.string.root_path_chat))

        initRcView()
        observer(rootPath)

        binding.apply {
            if (user != null) {
                ibOk.setOnClickListener {
                    if (edMessage.text.isNotEmpty()) {
                        rootPath.child(user.displayName ?: getString(R.string.error))
                            .setValue(
                                ChatItem(
                                    user.displayName.toString(),
                                    edMessage.text.toString(),
                                    TimeManager.getTime()
                                )
                            )
                        edMessage.setText("")
                    } else {
                        edMessage.error = getString(R.string.empty_filed)
                    }
                }
                ibDelete.setOnClickListener {
                    rootPath.child(user.displayName ?: "Error")
                        .removeValue()
                }
            } else {
                ibOk.isClickable = false
                ibDelete.isClickable = false
            }
        }
    }

    override fun onDestroy() {
        val rootPath = Firebase.database.getReference(getString(R.string.root_path_chat))
        rootPath.removeEventListener(valueEventListener)

        super.onDestroy()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        rcChatItems.layoutManager = LinearLayoutManager(requireContext())
        rcChatItems.adapter = adapter
        rcChatItems.setHasFixedSize(true)
    }

    private fun observer(rootPath: DatabaseReference) {
        rootPath.addValueEventListener(valueEventListener)
    }

    private fun createValueEventListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<ChatItem>()

                for (s in snapshot.children) {
                    val chatItem = s.getValue(ChatItem::class.java)
                    if (chatItem != null) list.add(chatItem)
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}