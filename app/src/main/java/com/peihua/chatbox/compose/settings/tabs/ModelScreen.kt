package com.peihua.chatbox.compose.settings.tabs


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ModelScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "模型")
    }
}


sealed class Container<T>(val value: T) {
    // 42? 宇宙的终极答案就是 42 啦
    class IntContainer: Container<Int>(42)
    // 这是本篇博客作者的名字
    class StringContainer: Container<String>("FunnySaltyFish")
}


//fun <V> unboxAndProcess(container: Container<V>): V =
//    when (container) {
//        is Container.IntContainer -> 42
//        // Actual type: String, Expected type: V
//        is Container.StringContainer -> "FunnySaltyFish"
//    }

fun <V> unboxAndProcess(container: Container<V>): V =
    when (container) {
        is Container.IntContainer -> container.value // V 会被推断为 Int
        is Container.StringContainer -> container.value // V 会被推断为 String
    }