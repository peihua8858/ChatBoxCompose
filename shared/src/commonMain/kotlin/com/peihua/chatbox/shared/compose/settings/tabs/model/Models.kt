package com.peihua.chatbox.shared.compose.settings.tabs.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


enum class ModelProvider(
    var model: Model,
    val contentView: @Composable (modifier: Modifier, provider: ModelProvider, modelChange: (ModelProvider) -> Unit) -> Unit,
) {
    /**
     * OpenAI
     */
    OPenAI(
        Model("https://api.openai.com",
        "gpt-3.5-turbo",
        "sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"),
        contentView = { modifier, provider, modelChange ->
            OpenAiSettingsContent(modifier, provider, modelChange)
        }),

    /**
     * DeepSeek
     */
    DeepSeek( Model("https://api.deepseek.com", "", ""), { modifier, provider, modelChange ->
        DeepSeekSettingsContent(modifier, provider, modelChange)
    }),

    /**
     * Gemini
     */
    Gemini( Model("https://api.gemini.com", "", ""), { modifier, provider, modelChange ->
        GeminiAiSettingsContent(modifier, provider, modelChange)
    });

    val displayName: String
        get() = name
}

data class Model(
    val host: String ,
    val model: String ,
    val apiKey: String,
    val temperature: Float = 0.7f,
    val meticulousCreative: Float = 0.7f,
    val topP: Float = 1f,
    val maxMessageCountInContext: Int = 10,
    val maxTokensInContext: Int = 1000,
    val maxTokensToGenerate: Int = 1000,

    )

val Chat_Models: ArrayList<ModelProvider>
    get() {
        val values = ModelProvider.entries
        val result = ArrayList<ModelProvider>()
        for (value in values) {
            result.add(value)
        }
        return result
    }