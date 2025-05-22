package com.example.chirp.viewModel.friends

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chirp.R
import com.example.chirp.data.service.FriendsService
import com.example.chirp.viewModel.Result
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch

class AddFriendViewModel(private val friendsService: FriendsService) : ViewModel() {

    private val _emailForm = MutableLiveData<Int?>()
    val emailForm: LiveData<Int?> = _emailForm

    private val _addFriendRequestResult = MutableLiveData<Result>()
    val addFriendRequestResult: LiveData<Result> = _addFriendRequestResult

    fun addFriend(email: String) {
        viewModelScope.launch {
            val response = friendsService.sendRequest(email)
            println(response)
            if (response.status == HttpStatusCode.OK) {
                _addFriendRequestResult.value = Result(true, "Send request")
            } else {
                _addFriendRequestResult.value = Result(false, "Failed: ${response.bodyAsText()}")
            }
        }
    }

    fun emailDataChanged(email: String) {
        val emailError = if (!isEmailNameValid(email)) R.string.invalid_email else null
        _emailForm.value = emailError
    }

    private fun isEmailNameValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}