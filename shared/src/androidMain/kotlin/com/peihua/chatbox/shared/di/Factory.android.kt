package com.peihua.chatbox.shared.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.peihua.chatbox.shared.LocalContext
import com.peihua.chatbox.shared.data.database.AppDatabase
import com.peihua.chatbox.shared.data.database.dbFileName
import com.peihua.chatbox.shared.http.HttpClient
import kotlinx.coroutines.Dispatchers

actual class Factory(private val app: Context) {
    actual fun createRoomDatabase(): AppDatabase {
        val dbFile = app.getDatabasePath(dbFileName)
        return Room.databaseBuilder<AppDatabase>(
            context = app,
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    actual fun createHttpClient(): io.ktor.client.HttpClient {
        return HttpClient{}
    }
}

actual fun FactoryImpl() : Factory {
    return Factory(app = LocalContext.context.applicationContext)
}
