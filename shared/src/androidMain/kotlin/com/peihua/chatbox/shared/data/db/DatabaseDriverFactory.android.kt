package com.peihua.chatbox.shared.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.peihua.chatbox.shared.LocalContext
import com.peihua.chatbox.shared.data.db.AppDatabase

actual fun createDriver(databaseName:String): SqlDriver {
    return AndroidSqliteDriver(AppDatabase.Companion.Schema, LocalContext.context, databaseName)
}