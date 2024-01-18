package com.chilcotin.familyapp.fragments.shareTodo

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.chilcotin.familyapp.App
import com.chilcotin.familyapp.R
import com.chilcotin.familyapp.databinding.FragmentEditShareTodoBinding
import com.chilcotin.familyapp.entities.ShareTodoItem
import com.chilcotin.familyapp.utils.Const
import com.chilcotin.familyapp.utils.TimeManager
import com.chilcotin.familyapp.viewmodel.MainViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditShareTodoFragment : Fragment() {
    private var _binding: FragmentEditShareTodoBinding? = null
    private val binding get() = _binding!!
    private var user = ""

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModel.MainViewModelFactory((context?.applicationContext as App).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(Const.UPDATE_SHARE_TODO_REQUEST) { _, bundle ->
            val result: ShareTodoItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(Const.UPDATE_SHARE_TODO, ShareTodoItem::class.java)
                    ?: ShareTodoItem(
                        getString(R.string.error)
                    )
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable(Const.UPDATE_SHARE_TODO)
                    ?: ShareTodoItem(getString(R.string.error))
            }
            binding.apply {
                edTitle.setText(result.title)
                edDescription.setText(result.description)
                tvTime.text = result.time
                checkBox.isChecked = result.checked
                user = result.creator
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditShareTodoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rootPath = Firebase.database.getReference(getString(R.string.root_path_share_todo))

        binding.apply {
            fbOkTask.setOnClickListener {
                if (edTitle.text.isEmpty()) {
                    edTitle.error = context?.getString(R.string.empty_filed)
                } else {
                    if (checkBoxUpdateDate.isChecked) {
                        mainViewModel.updateShareTodoItem(
                            ShareTodoItem(
                                edTitle.text.toString(),
                                edDescription.text.toString(),
                                TimeManager.getTime(),
                                checkBox.isChecked,
                                user
                            ), rootPath
                        )
                    } else {
                        mainViewModel.updateShareTodoItem(
                            ShareTodoItem(
                                edTitle.text.toString(),
                                edDescription.text.toString(),
                                tvTime.text.toString(),
                                checkBox.isChecked,
                                user
                            ), rootPath
                        )
                    }
                    findNavController().navigate(R.id.action_editShareTodoFragment_to_shareTodoFragment)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}