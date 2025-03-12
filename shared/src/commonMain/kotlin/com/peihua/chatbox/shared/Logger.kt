package com.peihua.chatbox.shared

import androidx.compose.runtime.Composable

abstract class Logger {
    companion object {
        /**
         * Priority constant for the println method; use Log.v.
         */
        val VERBOSE = 2;

        /**
         * Priority constant for the println method; use Log.d.
         */
        val DEBUG = 3;

        /**
         * Priority constant for the println method; use Log.i.
         */
        val INFO = 4;

        /**
         * Priority constant for the println method; use Log.w.
         */
        val WARN = 5;

        /**
         * Priority constant for the println method; use Log.e.
         */
        val ERROR = 6;

        /**
         * Priority constant for the println method.
         */
        val ASSERT = 7;
    }

    abstract fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String)
    @Composable
    abstract fun writeLog(tag: String, stackTraceIndex: Int, message: String)
}

expect fun logcat(): Logger
fun printLog(level: Int, stackTraceIndex: Int, tag: String, message: String) =
    logcat().printLog(level, stackTraceIndex, tag, message)
@Composable
fun writeLogToFile(tag: String, stackTraceIndex: Int, message: String) =
    logcat().writeLog(tag, stackTraceIndex, message)

