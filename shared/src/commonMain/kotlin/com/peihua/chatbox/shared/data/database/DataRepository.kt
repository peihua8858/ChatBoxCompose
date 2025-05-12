package com.peihua.chatbox.shared.data.database

import io.ktor.client.HttpClient

class DataRepository(
    private var database: AppDatabase,
    private var httpClient: HttpClient
) {
}