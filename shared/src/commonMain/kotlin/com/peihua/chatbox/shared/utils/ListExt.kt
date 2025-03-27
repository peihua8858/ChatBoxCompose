package com.peihua.chatbox.shared.utils

import kotlin.IllegalArgumentException


operator fun <T> List<T>.set(index: Int, menu: T) {
    if (this is ArrayList<T>) {
        this[index] = menu
    } else if (this is MutableList<T>) {
        this[index] = menu
    } else {
        throw IllegalArgumentException("List is not mutable")
    }
}