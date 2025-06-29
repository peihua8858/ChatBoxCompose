package com.peihua.chatbox.shared.data.remote.repository

import com.peihua.chatbox.shared.compose.settings.tabs.model.ModelProvider
import com.peihua.chatbox.shared.data.remote.repository.impl.DeepSeekRepositoryImpl
import com.peihua.chatbox.shared.data.remote.repository.impl.OpenAiRepositoryImpl

object ChatAiRepositoryFactory {
    fun create(model: ModelProvider): ChatAiRepository {
        return when (model) {
            ModelProvider.DeepSeek -> {
                DeepSeekRepositoryImpl()
            }

            ModelProvider.OPenAI -> {
                OpenAiRepositoryImpl()
            }

            else -> {
                OpenAiRepositoryImpl()
            }
        }
    }
}