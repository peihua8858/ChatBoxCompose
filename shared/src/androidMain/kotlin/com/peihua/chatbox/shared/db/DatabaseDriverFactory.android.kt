package com.peihua.chatbox.shared.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.peihua.chatbox.shared.LocalContext

actual fun createDriver(): SqlDriver {
    return AndroidSqliteDriver(AppDatabase.Schema, LocalContext.context, "test.db")
}