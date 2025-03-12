package com.peihua.chatbox.shared
import androidx.compose.runtime.Composable
import platform.Foundation.NSLog
 class IosLogger : Logger() {
     override fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) =
        NSLog("ChatBox", message)

     @Composable
     override fun writeLog(tag: String, stackTraceIndex: Int, message: String) {
         NSLog("ChatBox", message)
    }
}

private val logger: Logger = IosLogger()
actual fun logcat(): Logger = logger
