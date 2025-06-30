package com.peihua.chatbox.shared.data.remote.repository.impl

import com.peihua.chatbox.shared.data.remote.repository.ChatAiRepository
import com.peihua.chatbox.shared.http.HttpClient
import com.peihua.chatbox.shared.utils.isSuccess
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeepSeekRepositoryImpl : ChatAiRepository {
    private val httpClient by lazy { HttpClient { } }
    override fun textCompletionsWithStream(params: String): Flow<String> {
        return flow {
            val httpResponse: HttpResponse = httpClient.get("https://example-files.online-convert.com/document/txt/example.txt")
            if (httpResponse.isSuccess()) {
                val channel = httpResponse.bodyAsChannel()
                // 循环读取数据流
                emit("我是Deep Seek")
                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line()
                    if (!line.isNullOrEmpty()) {
                        emit(line)
                        delay(200)
                    }
                    println(line)
                }
            }
        }
    }
}