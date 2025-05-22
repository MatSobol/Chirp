package com.repository

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.utility.toDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import org.bson.Document
import org.bson.types.ObjectId

abstract class RepositoryImpl<T>(
    private val collection: MongoCollection<Document>,
    private val serializer: KSerializer<T>
) : Repository<T> {

    override suspend fun findById(id: String): Document? = withContext(Dispatchers.IO) {
        collection.find(Filters.eq("_id", ObjectId(id))).first()
    }

    override suspend fun insert(data: T): String = withContext(Dispatchers.IO) {
        val data = toDocument(data, serializer)
        collection.insertOne(data)
        data["_id"].toString()
    }

    override suspend fun update(id: String, data: T): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndReplace(Filters.eq("_id", ObjectId(id)), toDocument(data, serializer))
    }

    override suspend fun delete(id: String): Document? = withContext(Dispatchers.IO) {
        collection.findOneAndDelete(Filters.eq("_id", ObjectId(id)))
    }

}