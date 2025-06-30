package com.peihua.chatbox.shared.compose.settings.tabs.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

enum class Model(
    val host: String,
    val aiModel: String,
    val contentView: @Composable (modifier: Modifier, provider: ModelProvider, modelChange: (ModelProvider) -> Unit) -> Unit,
) {
    /**
     * OpenAI
     */
    OPenAI(
        "https://api.openai.com",
        "gpt-3.5-turbo",
        contentView = { modifier, provider, modelChange ->
            OpenAiSettingsContent(modifier, provider, modelChange)
        }),

    /**
     * DeepSeek
     */
    DeepSeek(
        "https://api.deepseek.com", "",
        contentView = { modifier, provider, modelChange ->
            DeepSeekSettingsContent(modifier, provider, modelChange)
        }),

    /**
     * Gemini
     */
    Gemini(
        "https://api.gemini.com", "",
        { modifier, provider, modelChange ->
            GeminiAiSettingsContent(modifier, provider, modelChange)
        });

    val displayName: String
        get() = name
}

@Serializable
data class ModelSettings(
    val model: Model,
    val host: String,
    val aiModel: String,
    val apiKey: String,
    val apiKeyPlaceholder: String = "",
    val temperature: Float = 0.7f,
    val meticulousCreative: Float = 0.7f,
    val topP: Float = 1f,
    val maxMessageCountInContext: Int = 10,
    val maxTokensInContext: Int = 1000,
    val maxTokensToGenerate: Int = 1000,

    ){
   companion object{
       fun default(): ModelSettings {
           return ModelSettings(
               model = Model.OPenAI,
               host = Model.OPenAI.host,
               aiModel = Model.OPenAI.aiModel,
               apiKey = "",
           )
       }
   }
}

data class ModelProvider(
    val model: Model,
    var settings: ModelSettings,
)
val Chat_Models: ArrayList<ModelProvider>
    get() {
        val values = Model.entries
        val result = ArrayList<ModelProvider>()
        for (value in values) {
            result.add(
                ModelProvider(
                    model = value,
                    settings = ModelSettings(
                        model = value,
                        host = value.host,
                        aiModel = value.aiModel,
                        apiKey = "",
                    ),
                )
            )
        }
        return result
    }