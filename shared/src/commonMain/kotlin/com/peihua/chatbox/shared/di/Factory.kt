package com.peihua.chatbox.shared.di

import com.peihua.chatbox.shared.data.database.AppDatabase
import com.peihua.chatbox.shared.data.database.AppDataStore
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

expect class Factory {
    fun createRoomDatabase(): AppDatabase
    fun createHttpClient(): HttpClient
    fun createDataStore(): AppDataStore
}


val json = Json { ignoreUnknownKeys = true }

expect fun FactoryImpl() : Factory
