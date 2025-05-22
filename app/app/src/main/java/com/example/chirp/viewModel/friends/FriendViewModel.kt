package com.example.chirp.viewModel.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chirp.data.model.Friend
import com.example.chirp.data.repositories.AccountRepository
import com.example.chirp.data.service.FriendsService
import com.example.chirp.viewModel.Result
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.launch

class FriendViewModel(
    private val friendsService: FriendsService,
    accountRepository: AccountRepository
) : ViewModel() {

    private val _friendsList = MutableLiveData<ArrayList<Friend>>()
    val friendsList: LiveData<ArrayList<Friend>> = _friendsList

    private val _friendsRequestList = MutableLiveData<ArrayList<Friend>>()
    val friendsRequestList: LiveData<ArrayList<Friend>> = _friendsRequestList

    private val _friendsRequestResult = MutableLiveData<Result>()
    val friendsRequestResult: LiveData<Result> = _friendsRequestResult

    init {
        val account = accountRepository.getAccount()
        println(account)
        _friendsList.value = account.friends
        _friendsRequestList.value = account.friendRequests
    }

    fun confirmRequest(friend: Friend) {
        viewModelScope.launch {
            val response = friendsService.addFriend(friend.id)
            if (response.status == HttpStatusCode.OK) {
                _friendsList.value = _friendsList.value?.let { ArrayList(it).apply { add(friend) } }
                _friendsRequestList.value =
                    ArrayList(_friendsRequestList.value?.filter { it.id != friend.id }
                        ?: emptyList())
                _friendsRequestResult.value =
                    Result(true, "added ${friend.name}")
            } else {
                _friendsRequestResult.value =
                    Result(false, response.bodyAsText())
            }

        }

    }

    fun denyRequest(id: String) {
        viewModelScope.launch {
            val response = friendsService.denyRequest(id)
            if (response.status == HttpStatusCode.OK) {
                _friendsRequestList.value =
                    ArrayList(_friendsRequestList.value?.filter { it.id != id } ?: emptyList())
                _friendsRequestResult.value =
                    Result(true, "Removed")
            } else {
                _friendsRequestResult.value =
                    Result(false, "Failed to remove")
            }

        }
    }

}