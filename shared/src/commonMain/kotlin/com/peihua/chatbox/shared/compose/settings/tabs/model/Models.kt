package com.peihua.chatbox.shared.compose.settings.tabs.model

import androidx.compose.runtime.Composable
import chatboxcompose.shared.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

enum class ModelProvider(val host: String) {
    /**
     * OpenAI
     */
    OPenAI("https://api.openai.com"),

    /**
     * DeepSeek
     */
    DeepSeek("https://api.deepseek.com"),

    /**
     * Gemini
     */
    Gemini("https://api.gemini.com");
    val displayName: String
        get() = name
}

data class Model(
    val provider: ModelProvider,
    val model: String = "gpt-3.5-turbo",
    val apiKey: String = "sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",
    val temperature: Float = 0.7f,
    val meticulousCreative: Float = 0.7f,
    val topP: Float = 1f,
    val maxMessageCountInContext: Int = 10,
    val maxTokensInContext: Int = 1000,
    val maxTokensToGenerate: Int = 1000,

    )