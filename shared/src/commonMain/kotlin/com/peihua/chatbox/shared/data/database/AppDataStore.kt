package com.peihua.chatbox.shared.data.database

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import com.peihua.chatbox.shared.compose.Settings
import com.peihua.chatbox.shared.compose.settings.tabs.display.TextScaler
import com.peihua.chatbox.shared.di.json
import com.peihua.chatbox.shared.theme.ThemeMode
import com.peihua.chatbox.shared.utils.WorkScope
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
        storage = OkioStorage<Settings>(
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
            get() = Settings(
                themeMode = ThemeMode.System,
                language = "zh",
                showAvatar = true,
                showWordCount = true,
                showTokenCount = true,
                showModelName = true,
                showTokenUsage = true,
                spellCheck = true,
                textScaler = TextScaler(1.0f, "Normal"),
            )

        override suspend fun readFrom(source: BufferedSource): Settings {
            return json.decodeFromString<Settings>(source.readUtf8())
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