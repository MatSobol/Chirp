package com.repository

import org.bson.Document

interface Repository<T> {
    suspend fun findById(id: String): Document?
    suspend fun insert(data: T): String
    suspend fun update(id: String, data: T): Document?
    suspend fun delete(id: String): Document?
}