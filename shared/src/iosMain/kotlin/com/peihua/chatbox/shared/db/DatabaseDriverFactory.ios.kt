package com.peihua.chatbox.shared.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual fun createDriver(databaseName:String): SqlDriver {
    return NativeSqliteDriver(AppDatabase.Schema, databaseName)
}