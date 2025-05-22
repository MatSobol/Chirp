package com.example.chirp.fragments.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.chirp.R
import com.example.chirp.databinding.FriendsFragmentFriendsBinding
import com.example.chirp.viewModel.friends.FriendViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FriendsFragment : Fragment() {
    private var _binding: FriendsFragmentFriendsBinding? = null
    private val binding get() = _binding!!

    private val friendViewModel: FriendViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FriendsFragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = binding.friendsTabLayout
        val viewPager = binding.friendsViewPager

        val adapter = FriendsPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.friends_tab_1_label)
                1 -> getString(R.string.friends_tab_2_label)
                else -> "Tab $position"
            }
            if (position == 1) {
                val badge = tab.getOrCreateBadge()
                badge.isVisible = true
                friendViewModel.friendsRequestList.observe(
                    viewLifecycleOwner
                ) { friendsRequestList ->
                    badge.number = friendsRequestList.size

                    badge.badgeTextColor = ContextCompat.getColor(requireContext(), R.color.default_color)
                    badge.backgroundColor = ContextCompat.getColor(requireContext(), R.color.active)
                }
            }
        }.attach()
    }
}