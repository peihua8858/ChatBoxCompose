package com.peihua.chatbox.shared.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.peihua.chatbox.shared.data.database.AppDataStore
import com.peihua.chatbox.shared.data.database.AppDatabase
import com.peihua.chatbox.shared.data.database.dbFileName
import com.peihua.chatbox.shared.http.HttpClient
import io.ktor.http.ContentType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class Factory() {
    actual fun createRoomDatabase(): AppDatabase {
        val dbFile = "${fileDirectory()}/database/$dbFileName"
        return Room.databaseBuilder<AppDatabase>(
            name = dbFile,
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    actual fun createHttpClient(): io.ktor.client.HttpClient {
        return HttpClient {}
    }

    actual fun createDataStore(): AppDataStore {
        val storeFile = "${fileDirectory()}/store"
        return AppDataStore(storePath = storeFile)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun fileDirectory(): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path!!
    }
}
private val mFactory: Factory by lazy {
    Factory()
}
actual fun FactoryImpl(): Factory {
    return mFactory
}