package com.peihua.chatbox.shared.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.darwin.Darwin

private val mHttpClient: HttpClient = HttpClient(Darwin) {
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}

actual fun HttpClient(block: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(Darwin) {
    block()
    engine {
        configureRequest {
            setAllowsCellularAccess(true)
        }
    }
}