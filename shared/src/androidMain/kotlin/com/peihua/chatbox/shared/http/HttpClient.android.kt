package com.peihua.chatbox.shared.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

private fun OkHttpClient.Builder.config(): OkHttpClient.Builder {
    followRedirects(false)
    followSslRedirects(false)
    retryOnConnectionFailure(true)
    connectTimeout(10L, TimeUnit.SECONDS)
    readTimeout(10L, TimeUnit.SECONDS)
    writeTimeout(10L, TimeUnit.SECONDS)
    callTimeout(10L, TimeUnit.SECONDS)
    return this
}

actual fun HttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient {
    return HttpClient(OkHttp) {
        block()
        engine {
            preconfigured = OkHttpClient.Builder().config().build()
            webSocketFactory = OkHttpClient.Builder().config().build()
            config {
                config()
                followRedirects(false)
                followSslRedirects(false)
                retryOnConnectionFailure(true)
                connectTimeout(10L, TimeUnit.SECONDS)
                readTimeout(10L, TimeUnit.SECONDS)
                writeTimeout(10L, TimeUnit.SECONDS)
                callTimeout(10L, TimeUnit.SECONDS)
            }
        }
    }
}