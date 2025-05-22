package com.utility

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.bson.Document

fun <T> toDocument(obj : T, serializer: KSerializer<T>): Document = Document.parse(Json.encodeToString(serializer, obj))

fun <T> fromDocument(document: Document, serializer: KSerializer<T>) : T {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(serializer, document.toJson())
}