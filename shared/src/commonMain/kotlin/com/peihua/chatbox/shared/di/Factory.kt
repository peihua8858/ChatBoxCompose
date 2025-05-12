package com.peihua.chatbox.shared.di

import com.peihua.chatbox.shared.data.database.AppDatabase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect class Factory {
    fun createRoomDatabase(): AppDatabase
    fun createHttpClient(): HttpClient
}


val json = Json { ignoreUnknownKeys = true }

expect fun FactoryImpl() : Factory
