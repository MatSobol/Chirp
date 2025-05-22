package com.example.chirp.fragments.chatsPreview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chirp.databinding.ChatsItemChatPreviewBinding
import com.example.chirp.data.model.ChatPreview

class ChatsPreviewAdapter :
    RecyclerView.Adapter<ChatsPreviewAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ChatsItemChatPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatPreview: ChatPreview) {
            binding.textViewTitle.text = chatPreview.title
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<ChatPreview>() {
        override fun areItemsTheSame(oldItem: ChatPreview, newItem: ChatPreview): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ChatPreview, newItem: ChatPreview): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(newList: List<ChatPreview>) {
        differ.submitList(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChatsItemChatPreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size
}



