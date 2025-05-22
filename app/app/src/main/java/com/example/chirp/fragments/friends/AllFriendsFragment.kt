package com.example.chirp.fragments.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chirp.R
import com.example.chirp.databinding.FriendsFragmentAllFriendsBinding
import com.example.chirp.viewModel.friends.FriendViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class AllFriendsFragment : Fragment() {
    private var _binding: FriendsFragmentAllFriendsBinding? = null
    private val binding get() = _binding!!
    private val friendViewModel: FriendViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FriendsFragmentAllFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        binding.searchFriendEditText.addTextChangedListener(afterTextChangedListener)
        binding.floatingActionButton3.setOnClickListener {
            val navController = this.findNavController()
            navController.navigate(R.id.action_nav_friendsFragment_to_addFriend)
        }
    }

    private fun setupRecycleView(): AllFriendsAdapter {
        val recyclerView: RecyclerView = binding.allFriendsRecycleView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AllFriendsAdapter()
        recyclerView.adapter = adapter
        friendViewModel.friendsList.observe(viewLifecycleOwner, Observer { friendsList ->
            println("hello")
            if (friendsList == null) {
                return@Observer
            }
            adapter.submitList(friendsList)
        })
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}