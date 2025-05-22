package com.example.chirp.fragments.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chirp.data.model.Friend
import com.example.chirp.databinding.FriendsItemFriendBinding

class AllFriendsAdapter :
    RecyclerView.Adapter<AllFriendsAdapter.ViewHolder>() {

    private var friendsList: ArrayList<Friend> = ArrayList()
    private var friendsListFiltered: ArrayList<Friend> = ArrayList()

    inner class ViewHolder(private val binding: FriendsItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: Friend) {
            binding.textView.text = friend.name
        }
    }

    fun submitList(newList: List<Friend>) {
        friendsList = newList as ArrayList
        friendsListFiltered = newList as ArrayList
        notifyDataSetChanged()
    }

    fun filter(value: String) {
        friendsListFiltered = friendsList.filter { el -> el.name.startsWith(value, ignoreCase = true) } as ArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FriendsItemFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(friendsListFiltered[position])
    }

    override fun getItemCount(): Int = friendsListFiltered.size
}