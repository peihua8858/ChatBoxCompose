package com.peihua.chatbox.shared.utils

import io.ktor.client.statement.HttpResponse


fun HttpResponse.isSuccess(): Boolean {
    return this.status.value in 200..299
}

fun HttpResponse.isError(): Boolean {
    return this.status.value in 400..599
}