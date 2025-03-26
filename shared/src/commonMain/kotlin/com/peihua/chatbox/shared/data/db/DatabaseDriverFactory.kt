package com.peihua.chatbox.shared.data.db

import app.cash.sqldelight.db.SqlDriver
import com.peihua.chatbox.shared.data.db.AppDatabase

expect fun createDriver(databaseName:String): SqlDriver

object DatabaseHelper {
    private lateinit var mDatabase: AppDatabase
    val database: AppDatabase
        get() {
            if (::mDatabase.isInitialized.not()) {
                val driver = createDriver("messages.db")
                mDatabase = AppDatabase.Companion(driver)
            }
            return mDatabase
        }

}