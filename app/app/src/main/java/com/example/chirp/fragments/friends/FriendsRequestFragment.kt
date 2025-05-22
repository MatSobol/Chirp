package com.example.chirp.fragments.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chirp.databinding.FriendsFragmentFriendsRequestBinding
import com.example.chirp.failAlert
import com.example.chirp.successAlert
import com.example.chirp.viewModel.friends.FriendViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FriendsRequestFragment : Fragment() {
    private var _binding: FriendsFragmentFriendsRequestBinding? = null
    private val binding get() = _binding!!
    private val friendViewModel: FriendViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FriendsFragmentFriendsRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        friendViewModel.friendsRequestResult.observe(
            viewLifecycleOwner
        ) { friendRequestResult ->
            context?.let { context ->
                if (friendRequestResult.isSuccess) successAlert(
                    context,
                    view,
                    friendRequestResult.message
                )
                else failAlert(context, view, friendRequestResult.message)
            }
        }
        val adapter = setupRecycleView()
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
            }
        }
        binding.searchRequestEditText.addTextChangedListener(afterTextChangedListener)
    }

    private fun setupRecycleView(): FriendsRequestAdapter {
        val recyclerView: RecyclerView = binding.friendsRequestRecycleView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter =
            FriendsRequestAdapter(friendViewModel::confirmRequest, friendViewModel::denyRequest)
        recyclerView.adapter = adapter
        friendViewModel.friendsRequestList.observe(viewLifecycleOwner,
            Observer { friendRequests ->
                println("requests hello")
                if (friendRequests == null) {
                    return@Observer
                }
                adapter.submitList(friendRequests)
            })
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}