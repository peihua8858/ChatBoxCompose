package com.peihua.chatbox.shared.data.database

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import com.peihua.chatbox.shared.compose.Settings
import com.peihua.chatbox.shared.di.json
import com.peihua.chatbox.shared.utils.WorkScope
import com.peihua.chatbox.shared.utils.eLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.use

class AppDataStore(private val storePath: String) {
    val settings: SettingsStore by lazy { SettingsStore(storePath) }
}

class SettingsStore(private val storePath: String) : CoroutineScope by WorkScope() {
    private val storeFile = "$storePath/settings.json"
    private val db = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = SettingsJsonSerializer,
            producePath = {
                storeFile.toPath()
            },
        ),
    )
    val data: Flow<Settings> = db.data
    fun getData(block: (Settings) -> Unit) {
        launch {
            data.collect {
                block(it)
            }
        }
    }

    fun updateSettings(settings: Settings) {
        launch {
            db.updateData { settings }
        }
    }

    internal object SettingsJsonSerializer : OkioSerializer<Settings> {
        override val defaultValue: Settings
            get() = Settings.default()

        override suspend fun readFrom(source: BufferedSource): Settings {
            return try {
                json.decodeFromString<Settings>(source.readUtf8())
            } catch (e: Throwable) {
                eLog { "readFrom error: ${e.message}" }
                // 如果反序列化失败，返回默认设置
                Settings.default()
            }
//         val jsonElement=   Json.decodeFromString<JsonElement>(source.readUtf8())
//            val displaySettings = jsonElement.jsonObject["display"]?.let {
//                Json.decodeFromJsonElement<DisplaySettings>(it)
//            } ?: DisplaySettings.default()
//
//            val modelSettings = jsonElement.jsonObject["aiModel"]?.let {
//                Json.decodeFromJsonElement<ModelSettings>(it)
//            } ?: ModelSettings.default()
//
//            val proxySettings = jsonElement.jsonObject["proxy"]?.let {
//                Json.decodeFromJsonElement<OtherSettings>(it)
//            } ?: OtherSettings.default()
//
//            return Settings(displaySettings, modelSettings, proxySettings)
        }

        override suspend fun writeTo(
            t: Settings,
            sink: BufferedSink,
        ) {
            sink.use {
                it.writeUtf8(json.encodeToString(Settings.serializer(), t))
            }
        }
    }
}