package com.peihua.chatbox.shared.data.remote.repository

import kotlinx.coroutines.flow.Flow

interface ChatAiRepository {
    fun textCompletionsWithStream(params: String): Flow<String>
}