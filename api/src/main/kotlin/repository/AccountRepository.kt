package com.repository

import com.model.Account.Account
import com.model.Account.AccountWithPassword
import com.model.Account.Friend
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.client.result.UpdateResult
import com.utility.toDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.Document
import org.bson.types.ObjectId

class AccountRepository(private val collection: MongoCollection<Document>) :
    RepositoryImpl<Account>(collection, Account.serializer()) {

    suspend fun register(account: AccountWithPassword): String = withContext(Dispatchers.IO) {
        val data = toDocument(account, AccountWithPassword.serializer())
        collection.insertOne(data)
        data["_id"].toString()
    }

    suspend fun findByEmail(email: String): Document? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq("email", email)).first()
    }

    suspend fun addFriend(id: String, friend: Friend): Boolean = withContext(Dispatchers.IO) {
        collection.updateOne(
            Filters.eq("_id", ObjectId(id)), Updates.push("friends", toDocument(friend, Friend.serializer()))
        ).wasAcknowledged()
    }

    suspend fun addFriendRequest(email: String, friendRequest: Friend): UpdateResult = withContext(Dispatchers.IO) {
        collection.updateOne(
            Filters.eq("email", email),
            Updates.addToSet("friendRequests", toDocument(friendRequest, Friend.serializer()))
        )
    }

    suspend fun removeFriendRequest(id: String, friendID: String): Boolean = withContext(Dispatchers.IO) {
        collection.updateOne(
            Filters.eq("_id", ObjectId(id)),
            Updates.pull("friendRequests", Filters.eq("id", friendID))
        ).wasAcknowledged()
    }
}