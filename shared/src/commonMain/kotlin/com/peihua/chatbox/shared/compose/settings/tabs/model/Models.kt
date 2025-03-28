package com.peihua.chatbox.shared.compose.settings.tabs.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


enum class ModelProvider(
    val host: String,
    val contentView: @Composable (modifier: Modifier, model: Model, modelChange: (Model) -> Unit) -> Unit,
) {
    /**
     * OpenAI
     */
    OPenAI("https://api.openai.com", contentView = { modifier, model, modelChange ->
        OpenAiSettingsContent(modifier, model, modelChange)
    }),

    /**
     * DeepSeek
     */
    DeepSeek("https://api.deepseek.com", { modifier, model, modelChange ->
        DeepSeekSettingsContent(modifier, model, modelChange)
    }),

    /**
     * Gemini
     */
    Gemini("https://api.gemini.com", { modifier, model, modelChange ->
        GeminiAiSettingsContent(modifier, model, modelChange)
    });

    val displayName: String
        get() = name
}

data class Model(
    val provider: ModelProvider,
    val host: String = provider.host,
    val model: String = "gpt-3.5-turbo",
    val apiKey: String = "sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    val temperature: Float = 0.7f,
    val meticulousCreative: Float = 0.7f,
    val topP: Float = 1f,
    val maxMessageCountInContext: Int = 10,
    val maxTokensInContext: Int = 1000,
    val maxTokensToGenerate: Int = 1000,

    )

val Chat_Models: ArrayList<Model>
    get() {
        val values = ModelProvider.entries
        val result = ArrayList<Model>()
        for (value in values) {
            result.add(Model(value))
        }
        return result
    }