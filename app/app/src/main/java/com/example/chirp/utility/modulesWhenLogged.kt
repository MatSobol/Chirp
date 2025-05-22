package com.example.chirp.utility

import com.example.chirp.data.repositories.AccountRepository
import com.example.chirp.data.service.AccountService
import com.example.chirp.data.service.Client
import com.example.chirp.data.service.FriendsService
import com.example.chirp.viewModel.friends.AddFriendViewModel
import com.example.chirp.viewModel.friends.FriendViewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

private val afterLoginModule = module {
    single { Client(get()) }
    single { AccountService(get()) }
    single { FriendsService(get()) }
    single { AccountRepository(get(), get()) }
    viewModel { FriendViewModel(get(), get()) }
    viewModel { AddFriendViewModel(get()) }
}

fun initAfterLogin() {
    loadKoinModules(afterLoginModule)
}

fun unload () {
    unloadKoinModules(afterLoginModule)
}