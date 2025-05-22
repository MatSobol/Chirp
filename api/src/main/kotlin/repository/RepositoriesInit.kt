package com.repository

import com.mongodb.client.MongoDatabase

interface Repositories {
    val accountRepository: AccountRepository
}

fun repositoriesInit (db: MongoDatabase) : Repositories{
    return object : Repositories {
        override val accountRepository = AccountRepository(db.getCollection("Account"))
    }
}
