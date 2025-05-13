package com.peihua.chatbox.shared.utils

import androidx.compose.runtime.Composable
import com.peihua.chatbox.shared.Logger
import com.peihua.chatbox.shared.printLog
import com.peihua.chatbox.shared.writeLogToFile

private const val STACK_TRACE_INDEX = 7

@Composable
fun writeLog(tagStr: String, log: String) {
    writeLogToFile(tagStr, STACK_TRACE_INDEX, log)
}

@Composable
fun ALog(lazyMessage: () -> String) {
    val message = lazyMessage()
    printLog(Logger.ASSERT, STACK_TRACE_INDEX, "", message)
}

fun <T> T.aLog(lazyMessage: () -> String): T {
    val message = lazyMessage()
    printLog(Logger.ASSERT, STACK_TRACE_INDEX, "", message)
    return this
}

@Composable
fun VLog(lazyMessage: () -> String) {
    val message = lazyMessage()
    printLog(Logger.VERBOSE, STACK_TRACE_INDEX, "", message)
}

fun <T> T.vLog(lazyMessage: () -> String): T {
    val message = lazyMessage()
    printLog(Logger.VERBOSE, STACK_TRACE_INDEX, "", message)
    return this
}

fun <T> T.wLog(lazyMessage: () -> String): T {
    val message = lazyMessage()
    printLog(Logger.WARN, STACK_TRACE_INDEX, "", message)
    return this
}

@Composable
fun ELog(lazyMessage: () -> String) {
    val message = lazyMessage()
    printLog(Logger.ERROR, STACK_TRACE_INDEX, "", message)
}

fun <T> T.eLog(lazyMessage: () -> String): T {
    val message = lazyMessage()
    printLog(Logger.ERROR, STACK_TRACE_INDEX, "", message)
    return this
}

@Composable
fun DLog(lazyMessage: () -> String) {
    val message = lazyMessage()
    printLog(Logger.DEBUG, STACK_TRACE_INDEX, "", message)
}

fun <T> T.dLog(lazyMessage: () -> String): T {
    val message = lazyMessage()
    printLog(Logger.DEBUG, STACK_TRACE_INDEX, "", message)
    return this
}
fun dLog(lazyMessage: () -> String){
    val message = lazyMessage()
    printLog(Logger.DEBUG, STACK_TRACE_INDEX, "", message)
}

fun <T> T.iLog(lazyMessage: () -> String): T {
    val message = lazyMessage()
    printLog(Logger.INFO, STACK_TRACE_INDEX, "", message)
    return this
}

fun Int.dLog(lazyMessage: () -> String): Int {
    val message = lazyMessage()
    printLog(Logger.DEBUG, STACK_TRACE_INDEX, "", message)
    return this
}

fun Double.dLog(lazyMessage: () -> String): Double {
    val message = lazyMessage()
    printLog(Logger.DEBUG, STACK_TRACE_INDEX, "", message)
    return this
}

fun Float.dLog(lazyMessage: () -> String): Float {
    val message = lazyMessage()
    printLog(Logger.DEBUG, STACK_TRACE_INDEX, "", message)
    return this
}

fun Long.dLog(lazyMessage: () -> String): Long {
    val message = lazyMessage()
    printLog(Logger.ERROR, STACK_TRACE_INDEX, "", message)
    return this
}


fun Int.eLog(lazyMessage: () -> String): Int {
    val message = lazyMessage()
    printLog(Logger.ERROR, STACK_TRACE_INDEX, "", message)
    return this
}

fun Double.eLog(lazyMessage: () -> String): Double {
    val message = lazyMessage()
    printLog(Logger.ERROR, STACK_TRACE_INDEX, "", message)
    return this
}

fun Float.eLog(lazyMessage: () -> String): Float {
    val message = lazyMessage()
    printLog(Logger.ERROR, STACK_TRACE_INDEX, "", message)
    return this
}

fun Long.eLog(lazyMessage: () -> String): Long {
    val message = lazyMessage()
    printLog(Logger.ERROR, STACK_TRACE_INDEX, "", message)
    return this
}

fun Int.wLog(lazyMessage: () -> String): Int {
    val message = lazyMessage()
    printLog(Logger.WARN, STACK_TRACE_INDEX, "", message)
    return this
}

fun Double.wLog(lazyMessage: () -> String): Double {
    val message = lazyMessage()
    printLog(Logger.WARN, STACK_TRACE_INDEX, "", message)
    return this
}

fun Float.wLog(lazyMessage: () -> String): Float {
    val message = lazyMessage()
    printLog(Logger.WARN, STACK_TRACE_INDEX, "", message)
    return this
}

fun Long.wLog(lazyMessage: () -> String): Long {
    val message = lazyMessage()
    printLog(Logger.WARN, STACK_TRACE_INDEX, "", message)
    return this
}

