package com.peihua.chatbox.shared.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.peihua.chatbox.shared.db.AppDatabase

actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(AppDatabase.Schema, "test.db")
}