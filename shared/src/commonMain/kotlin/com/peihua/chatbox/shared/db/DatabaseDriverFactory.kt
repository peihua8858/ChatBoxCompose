package com.peihua.chatbox.shared.db

import app.cash.sqldelight.db.SqlDriver

expect fun createDriver(): SqlDriver

object DatabaseHelper {
    private lateinit var mDatabase: AppDatabase
    val database: AppDatabase
        get() {
            if (::mDatabase.isInitialized.not()) {
                val driver = createDriver()
                mDatabase = AppDatabase(driver)
            }
            return mDatabase
        }

}