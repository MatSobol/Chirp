package com.example.chirp.fragments.friends

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chirp.databinding.FriendsFragmentAddFriendBinding
import com.example.chirp.failAlert
import com.example.chirp.successAlert
import com.example.chirp.utility.handleError
import com.example.chirp.utility.handleNoError
import com.example.chirp.viewModel.friends.AddFriendViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddFriend : Fragment() {
    private var _binding: FriendsFragmentAddFriendBinding? = null
    private val binding get() = _binding!!

    private val addFriendViewModel: AddFriendViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FriendsFragmentAddFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val submitButton = binding.addFriend
        val emailTextInputLayout = binding.addFriendEmail
        val emailEditText = binding.friendEmailEditText

        addFriendViewModel.emailForm.observe(viewLifecycleOwner) {
            it?.let {
                submitButton.isEnabled = false
                handleError(emailTextInputLayout, emailEditText, it, requireContext())
            } ?: run {
                submitButton.isEnabled = true
                handleNoError(emailTextInputLayout, emailEditText, requireContext())
            }
        }

        addFriendViewModel.addFriendRequestResult.observe(
            viewLifecycleOwner
        ) { addFriendRequestResult ->
            context?.let { context ->
                if (addFriendRequestResult.isSuccess) {
                    successAlert(
                        context,
                        view,
                        addFriendRequestResult.message
                    )
                    val navController = this.findNavController()
                    navController.navigateUp()
                } else failAlert(context, view, addFriendRequestResult.message)
            }
        }

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                addFriendViewModel.emailDataChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable) {
            }
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        submitButton.setOnClickListener { addFriendViewModel.addFriend(emailEditText.text.toString()) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}