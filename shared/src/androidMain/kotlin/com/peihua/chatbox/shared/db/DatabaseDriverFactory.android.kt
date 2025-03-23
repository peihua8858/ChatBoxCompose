package com.peihua.chatbox.shared.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.peihua.chatbox.shared.LocalContext

actual fun createDriver(databaseName:String): SqlDriver {
    return AndroidSqliteDriver(AppDatabase.Schema, LocalContext.context, databaseName)
}