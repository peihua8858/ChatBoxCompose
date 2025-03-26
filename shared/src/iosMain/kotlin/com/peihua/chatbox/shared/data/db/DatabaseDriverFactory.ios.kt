package com.peihua.chatbox.shared.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.peihua.chatbox.shared.db.AppDatabase

actual fun createDriver(databaseName:String): SqlDriver {
    return NativeSqliteDriver(AppDatabase.Companion.Schema, databaseName)
}