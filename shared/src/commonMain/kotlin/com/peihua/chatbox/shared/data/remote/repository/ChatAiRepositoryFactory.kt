package com.peihua.chatbox.shared.data.remote.repository

import com.peihua.chatbox.shared.compose.settings.tabs.model.Model
import com.peihua.chatbox.shared.compose.settings.tabs.model.ModelProvider

object ChatAiRepositoryFactory {
    fun create(model: Model): ChatAiRepository {
        return DeepSeekRepositoryImpl()
    }
}