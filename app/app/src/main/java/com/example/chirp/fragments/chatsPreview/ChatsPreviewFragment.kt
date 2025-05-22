package com.example.chirp.fragments.chatsPreview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chirp.R
import com.example.chirp.databinding.ChatsFragmentChatsPreviewBinding
import com.example.chirp.data.model.ChatPreview

class ChatsPreviewFragment : Fragment() {
    private var _binding: ChatsFragmentChatsPreviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ChatsFragmentChatsPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val items = listOf(
            ChatPreview("Chat 1"),
            ChatPreview("Chat 2"),
            ChatPreview("Chat 3")
        )

        val adapter = ChatsPreviewAdapter()
        binding.recyclerView.adapter = adapter
        adapter.submitList(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}