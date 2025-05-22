package com.example.chirp.fragments.friends

import androidx.lifecycle.lifecycleScope
import com.example.chirp.databinding.FriendsItemFriendRequestBinding
import com.example.chirp.failAlert
import com.example.chirp.successAlert
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chirp.data.model.Friend

class FriendsRequestAdapter(
    private val confirm: (Friend) -> Unit,
    private val deny: (String) -> Unit
) :
    RecyclerView.Adapter<FriendsRequestAdapter.ViewHolder>() {

    private var friendRequestsList: ArrayList<Friend> = ArrayList()
    private var friendRequestsListFiltered: ArrayList<Friend> = ArrayList()

    inner class ViewHolder(private val binding: FriendsItemFriendRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.confirmRequest.setOnClickListener {
                val item = friendRequestsListFiltered[adapterPosition]
                println(item)
                confirm(item)
            }
            binding.denyRequest.setOnClickListener {
                val id = friendRequestsListFiltered[adapterPosition].id
                deny(id)
            }
        }

        fun bind(friend: Friend) {
            binding.textView.text = friend.name
        }
    }

    fun submitList(newList: List<Friend>) {
        friendRequestsList = newList as ArrayList
        friendRequestsListFiltered = newList as ArrayList
        notifyDataSetChanged()
    }

    fun filter(value: String) {
        friendRequestsListFiltered =
            friendRequestsList.filter { el ->
                el.name.startsWith(
                    value,
                    ignoreCase = true
                )
            } as ArrayList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FriendsItemFriendRequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(friendRequestsListFiltered[position])
    }

    override fun getItemCount(): Int = friendRequestsListFiltered.size
}