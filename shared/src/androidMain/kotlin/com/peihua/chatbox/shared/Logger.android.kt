package com.peihua.chatbox.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class AndroidLogger : Logger() {
    override fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) {
        when (level) {
            VERBOSE -> Logcat.printLog(stackTraceIndex, Logcat.V, tag, message)
            DEBUG -> Logcat.printLog(stackTraceIndex, Logcat.D, tag, message)
            INFO -> Logcat.printLog(stackTraceIndex, Logcat.I, tag, message)
            WARN -> Logcat.printLog(stackTraceIndex, Logcat.W, tag, message)
            ERROR -> Logcat.printLog(stackTraceIndex, Logcat.E, tag, message)
        }
    }

    @Composable
    override fun writeLog(tag: String, stackTraceIndex: Int, message: String) {
        val context = LocalContext.current
        Logcat.writeLog(context, stackTraceIndex, tag, message)
    }
}

private val logger: Logger = AndroidLogger()
actual fun logcat(): Logger = logger