package com.peihua.chatbox.shared.di

import com.peihua.chatbox.shared.data.database.DataRepository

class AppContainer(
    private val factory: Factory,
) {
    val dataRepository: DataRepository by lazy {
        DataRepository(
            database = factory.createRoomDatabase(),
            httpClient = factory.createHttpClient(),
        )
    }
}

