package com.peihua.chatbox.shared.data.remote.repository

import com.peihua.chatbox.shared.compose.settings.tabs.model.Model
import com.peihua.chatbox.shared.compose.settings.tabs.model.ModelProvider
import com.peihua.chatbox.shared.data.remote.repository.impl.DeepSeekRepositoryImpl
import com.peihua.chatbox.shared.data.remote.repository.impl.OpenAiRepositoryImpl

object ChatAiRepositoryFactory {
    fun create(model: Model): ChatAiRepository {
        return when (model) {
            Model.DeepSeek -> {
                DeepSeekRepositoryImpl()
            }

            Model.OPenAI -> {
                OpenAiRepositoryImpl()
            }

            else -> {
                OpenAiRepositoryImpl()
            }
        }
    }
}